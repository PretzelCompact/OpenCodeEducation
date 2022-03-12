package stage2.practice.one;

import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;

public class AlphabetArchivator {
    private HashMap<Character, BitSet> symbolsToBits;
    private HashMap<BitSet, Character> bitsToSymbol;
    private int numOfBits;

    public AlphabetArchivator(char ... symbols){
        var set = new HashSet<Character>();
        for(var sym : symbols)
            set.add(sym);

        //ceiled nature log base 2
        numOfBits = (int)Math.ceil(Math.log(set.size())/Math.log(2));

        symbolsToBits = new HashMap<>();
        bitsToSymbol = new HashMap<>();

        int orderedNum = 0;
        for(var sym : set){
            var bits = new BitSet(numOfBits);

            for(int i = 0; i < numOfBits; i++){
                boolean bit = (orderedNum >> (numOfBits-i-1)) % 2 != 0;
                bits.set(i, bit);
            }

            symbolsToBits.put(sym,bits);
            bitsToSymbol.put(bits,sym);
            orderedNum++;
        }
    }

    public int getNumOfBits(){
        return numOfBits;
    }

    public BitSet compress(String str){
        var bitSet = new BitSet(str.length()*numOfBits);

        int total = 0;
        for(int i = 0; i < str.length(); i++){
            char curSym = str.charAt(i);
            var bitsOfSym = symbolsToBits.get(curSym);
            for(int j = 0; j < numOfBits; j++, total++){
                bitSet.set(total, bitsOfSym.get(j));
            }
        }
        return bitSet;
    }

    public String decompress(BitSet bits,int bitNumber){
        if(bitNumber%numOfBits != 0){
            throw new RuntimeException("Impossible to decompress because of invalid number of bits");
        }

        var sb = new StringBuilder();
        BitSet symbolBits;

        for(int i = 0; i < bitNumber; i+=numOfBits){
            symbolBits = bits.get(i,i+numOfBits);
            sb.append(bitsToSymbol.get(symbolBits));
        }
        return sb.toString();
    }

    @Override
    public String toString(){
        var sb = new StringBuilder();

        symbolsToBits.forEach((sym,bits)->{
            sb.append(sym + " - " + BitsString.get(bits,numOfBits) + '\n');
        });

        sb.delete(sb.length()-1, sb.length());
        return sb.toString();
    }
}
