package com.srgood.reasons.utils;

import com.google.common.base.Throwables;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

public class GitUtils {
    private static Repository repository = null;

    static {
        generateRepository();
    }

    public static Optional<String> getCurrentBranch() {
        return executeOnRepositorySafe(Repository::getBranch);
    }

    public static Optional<String> getCurrentRevision() {
        return executeOnRepositorySafe(repo -> repo.findRef(Constants.HEAD).getObjectId().getName());
    }

    private static <T> Optional<T> executeOnRepositorySafe(FallibleFunction<Repository, T> function) {
        if (repository == null) {
            return Optional.empty();
        }

        Function<Repository, T> safeFunction = exceptionProofFallibleFunction(function, null);
        return Optional.ofNullable(safeFunction.apply(repository));
    }

    private static <T, R> Function<T, R> exceptionProofFallibleFunction(FallibleFunction<T, R> function, R defaultValue) {
        return t -> {
            try {
                return function.apply(t);
            } catch (Exception e) {
                return defaultValue;
            }
        };
    }

    private static void generateRepository() {
        try {
            repository = new FileRepositoryBuilder().readEnvironment().findGitDir().setMustExist(true).build();
        } catch (IOException e) {
            Throwables.propagate(e);
        }
    }

    @FunctionalInterface
    private interface FallibleFunction<T, R> {
        R apply(T t) throws Exception;
    }
}
