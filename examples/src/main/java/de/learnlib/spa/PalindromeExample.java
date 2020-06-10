/* Copyright (C) 2019 Markus Frohme.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.learnlib.spa;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.learnlib.spa.api.SPA;
import de.learnlib.spa.api.SPAAlphabet;
import de.learnlib.spa.impl.DefaultSPA;
import de.learnlib.spa.impl.DefaultSPAAlphabet;
import de.learnlib.spa.palindrome.InputSymbol;
import net.automatalib.automata.fsa.impl.compact.CompactDFA;
import net.automatalib.util.automata.builders.AutomatonBuilders;
import net.automatalib.visualization.Visualization;
import net.automatalib.words.Alphabet;
import net.automatalib.words.impl.Alphabets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author frohme
 */
public final class PalindromeExample {

    private static final Logger LOGGER = LoggerFactory.getLogger(PalindromeExample.class);

    public static void main(String[] args) {

        final Alphabet<InputSymbol> callAlphabet = Alphabets.fromArray(InputSymbol.F, InputSymbol.G);
        final Alphabet<InputSymbol> internalAlphabet = Alphabets.fromArray(InputSymbol.a, InputSymbol.b, InputSymbol.c);

        final Set<InputSymbol> joinedSymbols = new HashSet<>();
        joinedSymbols.addAll(callAlphabet);
        joinedSymbols.addAll(internalAlphabet);

        final Alphabet<InputSymbol> joinedAlphabet = Alphabets.fromCollection(joinedSymbols);

        // @formatter:off
        final CompactDFA<InputSymbol> fProcedure = AutomatonBuilders.newDFA(joinedAlphabet)
                                                                    .withInitial("f0")
                                                                    .withAccepting("f0", "f1", "f2", "f5")
                                                                    .from("f0").on(InputSymbol.G).to("f5")
                                                                    .from("f0").on(InputSymbol.a).to("f1")
                                                                    .from("f0").on(InputSymbol.b).to("f2")
                                                                    .from("f1").on(InputSymbol.F).to("f3")
                                                                    .from("f2").on(InputSymbol.F).to("f4")
                                                                    .from("f3").on(InputSymbol.a).to("f5")
                                                                    .from("f4").on(InputSymbol.b).to("f5")
                                                                    .create();

        final CompactDFA<InputSymbol> gProcedure = AutomatonBuilders.newDFA(joinedAlphabet)
                                                                    .withInitial("g0")
                                                                    .withAccepting("g1", "g3")
                                                                    .from("g0").on(InputSymbol.F).to("g3")
                                                                    .from("g0").on(InputSymbol.c).to("g1")
                                                                    .from("g1").on(InputSymbol.G).to("g2")
                                                                    .from("g2").on(InputSymbol.c).to("g3")
                                                                    .create();
        // @formatter:on

        final Map<InputSymbol, CompactDFA<InputSymbol>> subModels = new HashMap<>();
        subModels.put(InputSymbol.F, fProcedure);
        subModels.put(InputSymbol.G, gProcedure);

        final SPAAlphabet<InputSymbol> alphabet =
                new DefaultSPAAlphabet<>(internalAlphabet, callAlphabet, InputSymbol.R);
        final SPA<?, InputSymbol> spa = new DefaultSPA<>(alphabet, InputSymbol.F, subModels);

        // @formatter:off
        LOGGER.info("Well-matched palindromes");
        logTrace(spa, InputSymbol.F, InputSymbol.R);
        logTrace(spa, InputSymbol.F, InputSymbol.a, InputSymbol.R);
        logTrace(spa, InputSymbol.F, InputSymbol.a, InputSymbol.F, InputSymbol.R, InputSymbol.a, InputSymbol.R);
        logTrace(spa, InputSymbol.F, InputSymbol.b, InputSymbol.F, InputSymbol.G, InputSymbol.c, InputSymbol.R, InputSymbol.R, InputSymbol.b, InputSymbol.R);

        LOGGER.info("Well-matched but invalid words");
        logTrace(spa, InputSymbol.F, InputSymbol.a, InputSymbol.a, InputSymbol.R);
        logTrace(spa, InputSymbol.F, InputSymbol.a, InputSymbol.G, InputSymbol.a, InputSymbol.R, InputSymbol.a, InputSymbol.R);
        logTrace(spa);

        LOGGER.info("Ill-matched/non-rooted words");
        logTrace(spa, InputSymbol.F, InputSymbol.F, InputSymbol.F);
        logTrace(spa, InputSymbol.R, InputSymbol.F);
        logTrace(spa, InputSymbol.a, InputSymbol.b, InputSymbol.a);
        // @formatter:on

        Visualization.visualize(spa);
    }

    @SafeVarargs
    private static <S, I> void logTrace(SPA<S, I> spa, I... inputs) {
        final List<I> asList = Arrays.asList(inputs);
        final boolean accepted = spa.computeOutput(asList);

        LOGGER.info("Word {} is {}accepted by the SPA", inputs, accepted ? "" : "not ");
    }

}
