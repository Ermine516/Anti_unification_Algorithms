package AntiUnif;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

import Terms.Term;

@FunctionalInterface
public interface Rule extends BiFunction<Configuration,Supplier<Term>,Optional<Stream<Configuration>>> {
    Optional<Stream<Configuration>> apply(Configuration config,Supplier<Term> variables);
}