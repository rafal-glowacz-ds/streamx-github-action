package dev.streamx.githhub.exception;

public class GithubActionException extends Exception {

  public GithubActionException(String message) {
    super(message);
  }

  public GithubActionException(String message, Throwable cause) {
    super(message, cause);
  }
}
