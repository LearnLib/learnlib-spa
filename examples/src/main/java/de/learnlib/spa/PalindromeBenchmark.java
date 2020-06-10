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
import java.util.Collection;

import de.learnlib.spa.util.AbstractCFGBenchmark;

/**
 * @author frohme
 */
public class PalindromeBenchmark extends AbstractCFGBenchmark {

    public static void main(String[] args) {
        new PalindromeBenchmark().runBenchmarkSuite();
    }

    @Override
    protected String getCFG() {
        // @formatter:off
        return
            "<F> -> a <F> a | b <F> b | <G> | a | b | \n" +
            "<G> -> c <G> c | c | <F>"
        ;
        // @formatter:on
    }

    @Override
    protected Collection<String> getStaticTraces() {
        return Arrays.asList("FbFRbFRbR", //no bFbFb in F
                             "FaFRaFRaR", // no aFaFa in F
                             "FGcGcRFRRR", // no cGF in G
                             "FGFRGcRcRR", // no FGc in G
                             "FGcGcRcGcRcRR" // no cGcGc in G
        );
    }
}
