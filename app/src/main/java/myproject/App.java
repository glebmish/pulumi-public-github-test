package myproject;

import com.pulumi.Pulumi;
import com.pulumi.core.Output;
import com.pulumi.deployment.InvokeOptions;
import com.pulumi.github.BranchDefault;
import com.pulumi.github.BranchDefaultArgs;
import com.pulumi.github.GithubFunctions;
import com.pulumi.github.Repository;
import com.pulumi.github.RepositoryArgs;
import com.pulumi.github.RepositoryCollaborator;
import com.pulumi.github.RepositoryCollaboratorArgs;
import com.pulumi.github.RepositoryFile;
import com.pulumi.github.RepositoryFileArgs;
import com.pulumi.github.inputs.GetRepositoryFileArgs;
import com.pulumi.github.inputs.RepositoryFileState;
import com.pulumi.resources.CustomResourceOptions;
import com.pulumi.resources.CustomTimeouts;

import java.time.Duration;
import java.util.List;

public class App {
    public static void main(String[] args) {
        Pulumi.run(ctx -> {
            var repo = new Repository("pulumi-new-project",
                RepositoryArgs.builder()
                    .name("pulumi-new-project")
                    .hasIssues(true)
                    .hasProjects(true)
                    .hasWiki(true)
                    .allowRebaseMerge(true)
                    .allowMergeCommit(true)
                    .allowSquashMerge(true)
                    .autoInit(true)
                    .build());
            var branch = new BranchDefault("pulumi-new-project",
                BranchDefaultArgs.builder()
                    .repository(repo.name())
                    .branch("main")
                    .build());
            new RepositoryFile("readme-md", RepositoryFileArgs.builder()
                .repository(repo.name())
                .branch(repo.defaultBranch())
                .file("README.md")
                .content(repo.name().apply(name -> Output.of("# " + name + "\n Provisioned by Pulumi")))
                .commitMessage("Managed by Pulumi")
                .overwriteOnCreate(true)
                .build());
        });
    }
}
