package dev.streamx.githhub.git.impl;

import dev.streamx.githhub.action.WebResourceAction;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.jboss.logging.Logger;

public class DiffResult {

  public static final DiffResult EMPTY_RESULT = new DiffResult();

  private static final Logger log = Logger.getLogger(DiffResult.class);

  private final Set<String> updated = new HashSet<>();

  private final Set<String> deleted = new HashSet<>();

  void add(DiffEntry entry) {
    log.debug("Adding entry: " + entry);
    log.debug("old -> new path: " + entry.getOldPath() + " -> " + entry.getNewPath());
    Optional.ofNullable(entry)
        .ifPresent(e -> {
          String path = Optional.ofNullable(e.getNewPath())
              .orElse(e.getOldPath());
          if (ChangeType.DELETE.equals(e.getChangeType())) {
            deleted.add(path);
          } else {
            updated.add(path);
          }
        });
  }

  public boolean isEmpty() {
    return updated.isEmpty() && deleted.isEmpty();
  }

  public Set<String> getModifiedPaths() {
    return updated;
  }

  public Set<String> getDeletedPaths() {
    return deleted;
  }

}
