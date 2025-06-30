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

    Set<String> result = FilesUtils.listFiles(testWorkspacePath.toAbsolutePath().toString());

    assertFalse(result.isEmpty());
    assertEquals(6, result.size());
    assertTrue(result.stream().allMatch(s -> s.contains(".css") || s.contains(".js")));
    assertTrue(result.contains("/root.css"));
    assertTrue(result.contains("/test_1/file_1_1.css"));
    assertTrue(result.contains("/test_1/file_1_2.css"));
    assertTrue(result.contains("/test_2/file_2_1.css"));
    assertTrue(result.contains("/test_2/file_2_1.js"));
    assertTrue(result.contains("/test_2/test_2_2/file_2_2_1.js"));
  }

  @Test
  public void testShouldListOnlyCssFiles() throws GithubActionException {
    Path testWorkspacePath = getTestWorkspacePath();

    Set<String> result = FilesUtils.listFilteredFiles(
        testWorkspacePath.toAbsolutePath().toString(), "**/*.css");

    assertFalse(result.isEmpty());
    assertEquals(4, result.size());
    assertTrue(result.stream().allMatch(s -> s.contains(".css")));
    assertTrue(result.contains("/root.css"));
    assertTrue(result.contains("/test_1/file_1_1.css"));
    assertTrue(result.contains("/test_1/file_1_2.css"));
    assertTrue(result.contains("/test_2/file_2_1.css"));
  }

  @Test
  public void testShouldListOnlyTest1CssFiles() throws GithubActionException {
    Path testWorkspacePath = getTestWorkspacePath();

    Set<String> result = FilesUtils.listFilteredFiles(
        testWorkspacePath.toAbsolutePath().toString(), "/test_1/*.css");

    assertFalse(result.isEmpty());
    assertEquals(2, result.size());
    assertTrue(result.stream().allMatch(s -> s.contains(".css")));
    assertTrue(result.contains("/test_1/file_1_1.css"));
    assertTrue(result.contains("/test_1/file_1_2.css"));
  }

  @Test
  public void testShouldListOnlyJsFiles() throws GithubActionException {
    Path testWorkspacePath = getTestWorkspacePath();

    Set<String> result = FilesUtils.listFilteredFiles(
        testWorkspacePath.toAbsolutePath().toString(), "**/*.js");

    assertFalse(result.isEmpty());
    assertEquals(2, result.size());
    assertTrue(result.stream().allMatch(s -> s.contains(".js")));
  }

  @Test
  public void testShouldListOnlyCssFilesFromOneFilter() throws GithubActionException {
    Path testWorkspacePath = getTestWorkspacePath();

    Set<String> result = FilesUtils.listFilteredFiles(
        testWorkspacePath.toAbsolutePath().toString(), "**/test_2/*.css");

    assertFalse(result.isEmpty());
    assertEquals(1, result.size());
    assertTrue(result.stream().allMatch(s -> s.contains(".css")));
  }

  @Test
  public void testShouldListOnlyJsFilesFromOneFilter() throws GithubActionException {
    Path testWorkspacePath = getTestWorkspacePath();

    Set<String> result = FilesUtils.listFilteredFiles(
        testWorkspacePath.toAbsolutePath().toString(), "**/test_2/*.js", "**/test_2/**/*.js");

    assertFalse(result.isEmpty());
    assertEquals(2, result.size());
    assertTrue(result.stream().allMatch(s -> s.contains(".js")));
  }


  private Path getTestWorkspacePath() {
    URL testWorkspace = this.getClass().getClassLoader().getResource("dev/streamx/github/files");
    Path testWorkspacePath = Path.of(testWorkspace.getPath());
    return testWorkspacePath;
  }
}