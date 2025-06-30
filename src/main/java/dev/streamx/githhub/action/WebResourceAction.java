package dev.streamx.githhub.action;

import dev.streamx.githhub.exception.GithubActionException;
import dev.streamx.githhub.git.GitService;
import dev.streamx.githhub.git.impl.DiffResult;
import dev.streamx.githhub.utils.FilesUtils;
import io.quarkiverse.githubaction.Action;
import io.quarkiverse.githubaction.Commands;
import io.quarkiverse.githubaction.Context;
import io.quarkiverse.githubaction.Inputs;
import io.quarkiverse.githubapp.event.PullRequest;
import io.quarkiverse.githubapp.event.WorkflowDispatch;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.jboss.logging.Logger;
import org.kohsuke.github.GHEventPayload;

@ApplicationScoped
public class WebResourceAction {

  private static final Logger log = Logger.getLogger(WebResourceAction.class);

  @Inject
  private GitService gitService;

  @Action
  void action(Commands commands) {
    commands.notice("Hello from Quarkus GitHub Action");

    commands.appendJobSummary(":wave: Hello from Quarkus GitHub Action");
  }

  @Action("pull_request")
  void webresourceSyncOnPullRequestMerged(Commands commands, Inputs inputs,
      @PullRequest GHEventPayload.PullRequest payload, Context context) throws IOException {
    commands.appendJobSummary(
        ":wave: Webresource Sync On Pull Request Merged from Quarkus GitHub Action");
    commands.notice("Here webresourceSyncOnPullRequestMerged from Quarkus GitHub Action");

    commands.notice("commands: " + Objects.isNull(commands));
    commands.notice("inputs: " + Objects.isNull(inputs));
    commands.notice("payload: " + Objects.isNull(payload));
    commands.notice("context: " + Objects.isNull(context));

    String jsonInputs = System.getenv("JSON_INPUTS");
    commands.notice("jsonInputs: " + jsonInputs);

    int commits = payload.getPullRequest().getCommits();
    commands.notice("Number of commits: " + commits);
    String workspace = context.getGitHubWorkspace();
    commands.notice("Workspace: " + workspace);

    try {
      DiffResult diffResult = gitService.getDiff(workspace, commits);
      if (diffResult.isEmpty()) {
        commands.notice(String.format("No changes detected at workspace: %s", workspace));
      } else {
        commands.notice(String.format("Changes detected at workspace: %s", workspace));
        Set<String> modifiedPaths = diffResult.getModifiedPaths();
        commands.notice(String.format("%d changes detected", modifiedPaths.size()));
        Set<String> deletedPaths = diffResult.getDeletedPaths();
        commands.notice(String.format("%d deletions detected", deletedPaths.size()));


      }
    } catch (GithubActionException exc) {
      log.error(exc.getMessage(), exc);
      commands.error(exc.getMessage());
    }
  }

  @Action("workflow_dispatch")
  void publishAll(Commands commands, @WorkflowDispatch GHEventPayload.WorkflowDispatch payload,
      Context context) {
    commands.notice("Hello from publishAll Quarkus GitHub Action");

    commands.appendJobSummary(":wave: Hello from publishAll Quarkus GitHub Action");

    String workspace = context.getGitHubWorkspace();
    commands.notice("Workspace: " + workspace);
    Map<String, Object> inputs = payload.getInputs();
    Object includeAntMatchPatterns = inputs.get("streamx-ingestion-webresource-includes");
    commands.notice("Includes AntMatch patterns: " + includeAntMatchPatterns);

    try {
      Set<String> files = FilesUtils.listFiles(workspace);

      for (String filePath : files) {
        commands.notice("file: " + filePath);
      }

    } catch (GithubActionException exc) {
      log.error(exc.getMessage(), exc);
      commands.error(exc.getMessage());
    }

  }

}
