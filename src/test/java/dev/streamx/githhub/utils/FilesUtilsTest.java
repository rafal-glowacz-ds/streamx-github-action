package dev.streamx.githhub.utils;


import static org.junit.jupiter.api.Assertions.*;

import dev.streamx.githhub.exception.GithubActionException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FilesUtilsTest {


  @Test
  public void testShouldListAllFiles() throws GithubActionException {
    Path testWorkspacePath = getTestWorkspacePath();

    Set<String> strings = FilesUtils.listFiles(testWorkspacePath.toAbsolutePath().toString());

    assertFalse(strings.isEmpty());
    assertEquals(6, strings.size());
    assertTrue(strings.stream().allMatch(s -> s.contains(".css") || s.contains(".js")));
  }

  @Test
  public void testShouldListOnlyCssFiles() throws GithubActionException {
    Path testWorkspacePath = getTestWorkspacePath();

    Set<String> strings = FilesUtils.listFilteredFiles(
        testWorkspacePath.toAbsolutePath().toString(), "**/*.css");

    assertFalse(strings.isEmpty());
    assertEquals(4, strings.size());
    assertTrue(strings.stream().allMatch(s -> s.contains(".css")));
  }

  @Test
  public void testShouldListOnlyJsFiles() throws GithubActionException {
    Path testWorkspacePath = getTestWorkspacePath();

    Set<String> strings = FilesUtils.listFilteredFiles(
        testWorkspacePath.toAbsolutePath().toString(), "**/*.js");

    assertFalse(strings.isEmpty());
    assertEquals(2, strings.size());
    assertTrue(strings.stream().allMatch(s -> s.contains(".js")));
  }

  @Test
  public void testShouldListOnlyCssFilesFromOneFilter() throws GithubActionException {
    Path testWorkspacePath = getTestWorkspacePath();

    Set<String> strings = FilesUtils.listFilteredFiles(
        testWorkspacePath.toAbsolutePath().toString(), "**/test_2/*.css");

    assertFalse(strings.isEmpty());
    assertEquals(1, strings.size());
    assertTrue(strings.stream().allMatch(s -> s.contains(".css")));
  }

  @Test
  public void testShouldListOnlyJsFilesFromOneFilter() throws GithubActionException {
    Path testWorkspacePath = getTestWorkspacePath();

    Set<String> strings = FilesUtils.listFilteredFiles(
        testWorkspacePath.toAbsolutePath().toString(), "**/test_2/*.js", "**/test_2/**/*.js");

    assertFalse(strings.isEmpty());
    assertEquals(2, strings.size());
    assertTrue(strings.stream().allMatch(s -> s.contains(".js")));
  }


  private Path getTestWorkspacePath() {
    URL testWorkspace = this.getClass().getClassLoader().getResource("dev/streamx/github/files");
    Path testWorkspacePath = Path.of(testWorkspace.getPath());
    return testWorkspacePath;
  }
}