package dev.streamx.githhub.git.impl;

import dev.streamx.githhub.git.GitService;
import jakarta.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.jboss.logging.Logger;

@ApplicationScoped
public class DefaultGitService implements GitService {

  public static final String CHANGED_REV_STR_FMT = "HEAD~%d^{tree}";
  private static final Logger log = Logger.getLogger(DefaultGitService.class);

  public DiffResult getDiff(String workspace, int commits) {
    if (StringUtils.isBlank(workspace) || commits == 0) {
      return DiffResult.EMPTY_RESULT;
    }

    Git git = this.getGit(workspace);
    if (Objects.isNull(git)) {
      log.error(String.format("Git repository not found for %s", workspace));
      return DiffResult.EMPTY_RESULT;
    }

    Repository repository = git.getRepository();
    try (ObjectReader reader = repository.newObjectReader()) {
      ObjectId head = repository.resolve("HEAD^{tree}");
      if (Objects.isNull(head)) {
        log.error(String.format("Git HEAD^{tree} can not resolve for %s", workspace));
      }
      String changedRevStr = String.format(CHANGED_REV_STR_FMT, commits);
      ObjectId changes = repository.resolve(changedRevStr);
      if (Objects.isNull(changes)) {
        log.error(String.format("Git %s can not resolve for %s", changedRevStr, workspace));
      }

      CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
      oldTreeIter.reset(reader, changes);
      CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
      newTreeIter.reset(reader, head);
      DiffResult diffResult = new DiffResult();
      git.diff()
          .setShowNameAndStatusOnly(true)
          .setNewTree(newTreeIter)
          .setOldTree(oldTreeIter)
          .call()
          .forEach(diffResult::add);
      return diffResult;
    } catch (GitAPIException e) {
      log.error("Diff GIT command has failed: " + e.getMessage(), e);
      return DiffResult.EMPTY_RESULT;
    } catch (IOException e) {
      log.error("Diff GIT execution has failed: " + e.getMessage(), e);
      return DiffResult.EMPTY_RESULT;
    }
  }

  private Git getGit(String workspace) {
    return Optional.ofNullable(workspace)
        .map(File::new)
        .map(p -> {
          try {
            return Git.open(p);
          } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
          }
        })
        .orElse(null);
  }
}
