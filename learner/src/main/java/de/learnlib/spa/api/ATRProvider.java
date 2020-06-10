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
package de.learnlib.spa.api;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import de.learnlib.api.AccessSequenceTransformer;
import net.automatalib.automata.fsa.DFA;
import net.automatalib.words.Word;

public interface ATRProvider<I> {

    Word<I> getAccessSequence(I procedure);

    Word<I> getTerminatingSequence(I procedure);

    Word<I> getReturnSequence(I procedure);

    Set<I> scanPositiveCounterexample(Word<I> counterexample);

    /**
     * @param procedures
     *
     * @return {@code true} if terminating sequences have been modified
     */
    void scanRefinedProcedures(Map<I, ? extends DFA<?, I>> procedures,
                               Map<I, ? extends AccessSequenceTransformer<I>> providers,
                               Collection<I> inputs);

}
