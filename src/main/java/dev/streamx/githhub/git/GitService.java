package dev.streamx.githhub.git;

import dev.streamx.githhub.git.impl.DiffResult;

public interface GitService {

  DiffResult getDiff(String workspace, int commits);
}
