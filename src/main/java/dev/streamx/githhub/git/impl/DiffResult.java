package dev.streamx.githhub.git.impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;

public class DiffResult {

  public static final DiffResult EMPTY_RESULT = new DiffResult();

  private final Set<DiffEntry> updated = new HashSet<>();

  private final Set<DiffEntry> deleted = new HashSet<>();

  void add(DiffEntry entry) {
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
