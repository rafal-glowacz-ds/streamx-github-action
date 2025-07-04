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

  private final Set<DiffEntry> updated = new HashSet<>();

  private final Set<DiffEntry> deleted = new HashSet<>();

  void add(DiffEntry entry) {
    log.debug("Adding entry: " + entry);
    Optional.ofNullable(entry)
        .ifPresent(e -> {
          if (ChangeType.DELETE.equals(e.getChangeType())) {
            deleted.add(e);
          } else {
            updated.add(e);
          }
        });
  }

  public boolean isEmpty() {
    return updated.isEmpty() && deleted.isEmpty();
  }

  public Set<String> getModifiedPaths() {
    return deleted.stream()
        .map(DiffEntry::getNewPath)
        .collect(Collectors.toSet());
  }

  public Set<String> getDeletedPaths() {
    return deleted.stream()
        .map(DiffEntry::getNewPath)
        .collect(Collectors.toSet());
  }

}
