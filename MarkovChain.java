
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.*;

public class MarkovChain {
    HashMap<String, TreeMap<String, Float>> chain;
    Set<String> known_words;
    Random rand;


    public static void main(String[] args) {
        MarkovChain chain = new MarkovChain("This reason doth not forth in mind had decided to melt in his two equidistant rays and with indignant man gazed with wholesome for the sovereign attempt was not glad to hazaraddar. Think you not with thy words with an addition to him and was wont to do not know him on this nice customs officer smiled enough to him i had i dwell in earth and i might have a girl came crowding throng of it clings round and strike at his horse that would have often be it was held it to see lothario were the wayfaring men were so happy and showed me not to huram sent to be provocative and said he sit in the mode of the state of imagination of the people had taken you told you infernal pit were saints shall he might be seen for he said rost. There longer be no one of the colonel monteynard. Sometimes from me not expect no self to invite lord was forbidden it is incapable and seized him of one of the cataract of which are you won the project gutenberg license included under which the project gutenberg volunteers and reached which were his style as tedious talkers are not a modified in britain having a notary said she cleaned it had drawn the gas from time uttered in iceland dog was not then there you cause which to the old count dracula is the air of their style of crisae. And was the peculiarities of promoting the upper step by his place where the funeral games of plane tree a moral causes perchance attain my credit seldom happened one day is there was reluctant elbow on the prisoners convicted by her what i saw the fact is little at magnesia is my story was happy to dress with laughter and the least in play the first senator tried his throne beheld the money and humanity to take a castle and three o master i mean literally discoloured by an attempt an upper lip thank him a prisoner that time of engravings of future might be sure as i spent over poor crittur ye shall tend to be happy if the earth as a fur coat bore little snipe showed him so i suppose it to that of me happy were rather be wool or actions which the sisters were not trouble again by the other apparatus for what do justice to whisper tales and is not the searchers or models and returned from necessity of his name hath the kitchen door which make them all been painted bow and st his solitude into an old woman with them go along the renegade must wholly contradictory of young puritan character from the lord triton down their faces on them by her large garden of montalvan figures in despite these are involved in the risk of the ascendancy to receive the law of ultimately made very kind of the mischance be of the senior artillery of the lord junius brutus and made with the enemy at her in the table and stretch themselves into the man born bilious man move thee to take lodgings near a fly into his family conclave or crossing and his brother and not that thou didst thou wert thou son looked with the mire and miracles before the word about the conception of the walk hee from that the elements at them in a huge carts in a philanthropist too was made a nut cakes for her again interchanged a hundred thousand blue in lieu of those wonderful an alteration of death conveying to him to be a provision of the spear of my dear lord god is in whose courage as happy and necessary being weak state i was very clear the prose and now among those navigators to examine the night of life a sort of the yearly enjoynd. The sole cause of the centre in thee from afar what wilt hear the supply his revenue service and walked along the time and the balance of all hail and he with it a fact which is your godfather also unto his pen put one aggregate character for although i do his mother stop her i and properties of duty and the officer named dumollard. London upon the library that you plunge him working her talking together with those two points at the petticoat and ask of the imputation of the tenor of arms in thy people observed that case of the _houyhnhnms_ know not only by sea into thy request at the same origin of those who had been assured him and this second servant went to justify the boy came with anyone was life are now you the regressus in the trunk separated from the midst of the eyes shining faintly marked for his cap and the first guard not on the like a little cockney voice outside the transactions by eva soon exhausted by those he slay her carriage door was not hear the beings was at least brave and aid of the value of which ne and all by which was over into young mind saying by reading out to go fast rising from other taxes upon the brush them will the marriage the social and she was indebted to remain freely distributed so strong above his argol. He at him gladly and followed one to complain of her with a stone floor was a string of a pitch of the gate the wide hovering near the garment quite close my people jump in the lord be made worse by chance is better wife drew you have bread him what i think of your clothes only five thousand francs for his election there being mistaken savage templar should it was that we venture upon the cravings of bringing them honourable w in the lateral movement of delacroix. Donalbain be passed from calcutta in the village are cunning to poor woman with the general utility or charges");
    }

    /**
     * Generates a first order Markov Chain from the given text
     * @param input_text The text to parse
     */
    public MarkovChain(String input_text) {
        init(input_text, 1);
        String bigramString = "is in";
        StringTokenizer st = new StringTokenizer(bigramString);
        while (st.hasMoreTokens()) {
            String firstString = st.nextToken();
            Map<String, Float> wordMap = chain.get(firstString);
            String secondString = st.nextToken();

            Float probability = wordMap.get(secondString);

            Map<String, Float> secondWordMap = chain.get(secondString);
            TreeMap<String, Float> reverseSortMap = new TreeMap(Collections.reverseOrder());
            for ( Map.Entry<String, Float> entry : secondWordMap.entrySet()) {
                String thirdWord = entry.getKey();
                Float thirdProbability = entry.getValue();
                // do something with key and/or tab
                System.out.println(" Word Combo ("+firstString+","+secondString+","+thirdWord+") : Probability = "+probability*thirdProbability);
                reverseSortMap.put("("+firstString+","+secondString+","+thirdWord+")",probability*thirdProbability);
            }
            System.out.println("The most probable word combo after "+bigramString + " is "+reverseSortMap.firstKey()+" with probability = "+reverseSortMap.firstEntry());
        }

    }

    /**
     * Generates a nth order Markov Chain from the given text
     * @param input_text The text to parse
     * @param n The order of the Markov Chain
     */
    public MarkovChain(String input_text, int n) {
        init(input_text, n);
    }

    /**
     * Reads a Markov Chain from the given input stream. The object is assumed
     * to be binary and serialized
     * @param in The input stream, eg from a network port or file
     */
    public MarkovChain(InputStream in) {
        try {
            ObjectInputStream ob_in = new ObjectInputStream(in);
            chain = (HashMap<String, TreeMap<String, Float>>)ob_in.readObject();
            known_words = chain.keySet();
            ob_in.close();
            in.close();
        } catch (IOException e) {
            //e.printStackTrace();
            chain = null;
            known_words = null;
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            chain = null;
            known_words = null;
        }
    }

    /**
     * Returns the next word, according to the Markov Chain probabilities 
     * @param current_word The current generated word
     */
    public String nextWord(String current_word) {
        if(current_word == null) return nextWord();

        // Then head off down the yellow-markov-brick-road
        TreeMap<String, Float> wordmap = chain.get(current_word);
        if(wordmap == null) {
            /* This *shouldn't* happen, but if we get a word that isn't in the
             * Markov Chain, choose another random one
             */
            return nextWord();
        }

        // Choose the next word based on an RV (Random Variable)
        float rv = rand.nextFloat();
        for(String word : wordmap.keySet()) {
            float prob = wordmap.get(word);
            rv -= prob;
            if(rv <= 0) {
                return word;
            }
        }

        /* We should never get here - if we do, then the probabilities have
         * been calculated incorrectly in the Markov Chain
         */
        assert false : "Probabilities in Markov Chain must sum to one!";
        return null;
    }

    /**
     * Returns the next word when the current word is unknown, irrelevant or
     * non existant (at the start of the sequence - randomly picks from known_words
     */
    public String nextWord() {
        return (String) known_words.toArray()[rand.nextInt(known_words.size())];
    }

    private void init(String input_text, int n) {
        if(input_text.length() <= 0) return;
        if(n <= 0) return;

        chain = new HashMap<String, TreeMap<String, Float>>();
        known_words = new HashSet<String>();
        rand = new Random(new Date().getTime());

        /** Generate the Markov Chain! **/
        StringTokenizer st = new StringTokenizer(input_text);

        while (st.hasMoreTokens()) {
            String word = st.nextToken();
            TreeMap<String, Float> wordmap = new TreeMap<String, Float>();

            // First check if the current word has previously been parsed
            if(known_words.contains(word)) continue;
            known_words.add(word);

            // Build the Markov probability table for this word
            StringTokenizer st_this_word = new StringTokenizer(input_text);
            String previous = "";
            while (st_this_word.hasMoreTokens()) {
                String next_word = st_this_word.nextToken();

                if(previous.equals(word)) {
                    if(wordmap.containsKey(next_word)) {
                        // Increment the number of counts for this word by 1
                        float num = wordmap.get(next_word);
                        wordmap.put(next_word, num + 1);
                    } else {
                        wordmap.put(next_word, 1.0f);
                    }
                }

                previous = next_word;
            } // End while (st_this_word.hasMoreTokens())

            /* The wordmap now contains a map of words and the number of occurrences they have.
             * We need to convert this to the probability of getting that word by dividing
             * by the total number of words there were
             */
            int total_number_of_words = wordmap.values().size();
            for(String k : wordmap.keySet()) {
                int num_occurances = wordmap.get(k).intValue();
                wordmap.put(k, 1.0f*num_occurances/total_number_of_words);
            }

            // Finally, we are ready to add this word and wordmap to the Markov chain
            chain.put(word, wordmap);
            System.out.println("Inserting word = "+word+" and wordmap which is = "+wordmap.toString());

        } // End while (st.hasMoreTokens())

        // The (first order) Markov Chain has now been built!
    }
}