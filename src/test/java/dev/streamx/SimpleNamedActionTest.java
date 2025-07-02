package dev.streamx;

import static dev.streamx.githhub.action.WebResourceAction.STREAMX_INGESTION_BASE_URL;
import static dev.streamx.githhub.action.WebResourceAction.STREAMX_INGESTION_WEBRESOURCE_INCLUDES;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.streamx.SimpleNamedActionTest.SimpleNamedActionTestProfile;
import io.quarkiverse.githubaction.Context;
import io.quarkiverse.githubaction.ContextInitializer;
import io.quarkiverse.githubaction.Inputs;
import io.quarkiverse.githubaction.InputsInitializer;
import io.quarkiverse.githubaction.testing.DefaultTestContext;
import io.quarkiverse.githubaction.testing.DefaultTestInputs;
import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.junit.main.Launch;
import io.quarkus.test.junit.main.LaunchResult;
import io.quarkus.test.junit.main.QuarkusMainTest;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Singleton;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

@QuarkusMainTest
@TestProfile(SimpleNamedActionTestProfile.class)
public class SimpleNamedActionTest {

    @Test
    @Launch(value = {})
    public void testLaunchCommand(LaunchResult result) {
        assertTrue(result.getOutput().contains("workflow_dispatch"));
    }

    public static class SimpleNamedActionTestProfile implements QuarkusTestProfile {

        @Override
        public Set<Class<?>> getEnabledAlternatives() {
            return Set.of(MockInputsInitializer.class, MockContextInitializer.class);
        }
    }

    @Alternative
    @Singleton
    public static class MockInputsInitializer implements InputsInitializer {


        @Override
        public Inputs createInputs() {
            return new DefaultTestInputs(Map.of(
                Inputs.ACTION, "webresource_workflow_dispatch",
                STREAMX_INGESTION_BASE_URL, "http://ingestion.127.0.0.1.nip.io",
                STREAMX_INGESTION_WEBRESOURCE_INCLUDES, "[\"scripts/*.js\", \"styles/*.css\"]"
            ));
        }
    }

    @Alternative
    @Singleton
    public static class MockContextInitializer implements ContextInitializer {

        @Override
        public Context createContext() {
            return new DefaultTestContext() {

                @Override
                public String getGitHubWorkspace() {
                    return "/Users/orodis/projects/streamx/code/fork/demo-puresight-eds-next";
                }

            };
        }
    }
}
