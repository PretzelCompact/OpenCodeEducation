package stage2.practice.one;

import java.util.BitSet;

public class BitsString {
    public static String get(BitSet bits, int numOfBits){
        var sb = new StringBuilder();
        for(int i = 0; i < numOfBits; i++)
            sb.append(bits.get(i) ? '1' : '0');
        return sb.toString();
    }
    public static BitSet convert(String str){
        var bits = new BitSet(str.length());
        char zero = '0';
        char one = '1';

        for(int i = 0; i < str.length(); i++){
            char curSym = str.charAt(i);
            if(curSym != zero && curSym != one)
                throw  new RuntimeException("String you are trying to convert doesn't represent binary line");
            bits.set(i,curSym==one);
        }

        return  bits;
    }
}
