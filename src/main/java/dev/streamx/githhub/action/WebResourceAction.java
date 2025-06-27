package dev.streamx.githhub.action;

import io.quarkiverse.githubaction.Action;
import io.quarkiverse.githubaction.Commands;
import io.quarkiverse.githubaction.Inputs;
import io.quarkiverse.githubapp.event.PullRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.kohsuke.github.GHEventPayload;

public class WebResourceAction {

  @Action
  void action(Commands commands) {
    commands.notice("Hello from Quarkus GitHub Action");

    commands.appendJobSummary(":wave: Hello from Quarkus GitHub Action");
  }

  @Action("webresource-sync-on-pullrequest-merged")
  void webresourceSyncOnPullRequestMerged(Commands commands, Inputs inputs,
      @PullRequest GHEventPayload.PullRequest payload) throws IOException {
    commands.appendJobSummary(
        ":wave: Webresource Sync On Pull Request Merged from Quarkus GitHub Action");
    commands.notice("Here webresourceSyncOnPullRequestMerged from Quarkus GitHub Action");

    int commits = payload.getPullRequest().getCommits();
    commands.notice("Number of commits: " + commands);

    String githubToken = inputs.getRequired("GITHUB_TOKEN");
    commands.notice("GithubToken: " + githubToken);

//    CloneCommand cloneCommand = Git.cloneRepository()
//        .setDirectory(Path.of("./repo").toFile())
//        .setURI("https://github.com/my-username/my-repo.git")
//        .setCredentialsProvider(
//            new UsernamePasswordCredentialsProvider(githubToken, StringUtils.EMPTY));

    File gitDirectory = Path.of(".").toFile();
    commands.notice("gitDirectory: " + gitDirectory);

    Git git = Git.open(gitDirectory);
    Repository repository = git.getRepository();

    ObjectId head = repository.resolve("HEAD");
    ObjectId changes = repository.resolve("HEAD^" + commits);

    try (ObjectReader reader = repository.newObjectReader()) {
      CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
      oldTreeIter.reset(reader, changes);
      CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
      newTreeIter.reset(reader, head);

      List<DiffEntry> diffs = git.diff()
          .setShowNameAndStatusOnly(true)
          .setNewTree(newTreeIter)
          .setOldTree(oldTreeIter)
          .call();
      for (DiffEntry entry : diffs) {
        ChangeType changeType = entry.getChangeType();
        if (changeType == ChangeType.DELETE) {
          commands.debug("Deleted file " + entry.getNewPath());
        } else {
          commands.debug("Changed file " + entry.getNewPath());
        }
      }

    } catch (InvalidRemoteException e) {
      throw new RuntimeException(e);
    } catch (TransportException e) {
      throw new RuntimeException(e);
    } catch (GitAPIException e) {
      throw new RuntimeException(e);
    }
  }

  //@Action("webresource-unpublish")
  void unpublish(Commands commands) {
    commands.debug("Action unpublish");
  }

  @Action("webresource-publish-all")
  void publishAll(Commands commands) {
    commands.notice("Hello from publishAll Quarkus GitHub Action");

    commands.appendJobSummary(":wave: Hello from publishAll Quarkus GitHub Action");
  }

}
