package org.demo.urlshortner.helper;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Component
public class UniqueIdCreator {

    private static UniqueIdCreator instance;

    private UniqueIdCreator() {
        initializeCharToIndexTable();
        initializeIndexToCharTable();
    }

    public static UniqueIdCreator getInstance(){
        if(null == instance) {
            instance = new UniqueIdCreator();
        }
        return instance;
    }

    private static HashMap<Character, Integer> charToIndexTable;
    private static List<Character> indexToCharTable;

    private void initializeCharToIndexTable() {
        charToIndexTable = new HashMap<>();
        // 0->a, 1->b, ..., 25->z, ..., 52->0, 61->9
        for (int i = 0; i < 26; ++i) {
            char c = 'a';
            c += i;
            charToIndexTable.put(c, i);
        }
        for (int i = 26; i < 52; ++i) {
            char c = 'A';
            c += (i-26);
            charToIndexTable.put(c, i);
        }
        for (int i = 52; i < 62; ++i) {
            char c = '0';
            c += (i - 52);
            charToIndexTable.put(c, i);
        }
    }

    private void initializeIndexToCharTable() {
        // 0->a, 1->b, ..., 25->z, ..., 52->0, 61->9
        indexToCharTable = new ArrayList<>();
        for (int i = 0; i < 26; ++i) {
            char c = 'a';
            c += i;
            indexToCharTable.add(c);
        }
        for (int i = 26; i < 52; ++i) {
            char c = 'A';
            c += (i-26);
            indexToCharTable.add(c);
        }
        for (int i = 52; i < 62; ++i) {
            char c = '0';
            c += (i - 52);
            indexToCharTable.add(c);
        }
    }

    public String createUniqueId(Long id) {
        List<Integer> base62Id = convertbase10Tobase62Id(id);
        StringBuilder uniqueUrlId = new StringBuilder();
        for (int digit: base62Id) {
            uniqueUrlId.append(indexToCharTable.get(digit));
        }
        return uniqueUrlId.toString();
    }
    private List<Integer> convertbase10Tobase62Id(Long id) {
        List<Integer> digits = new LinkedList<>();
        while(id > 0) {
            int remainder = (int)(id % 62);
            ((LinkedList<Integer>) digits).addFirst(remainder);
            id /= 62;
        }
        return digits;
    }

    public Long retrieveUniqueId(String uniqueId) {
        List<Character> base62Number = new ArrayList<>();
        for (int i = 0; i < uniqueId.length(); ++i) {
            base62Number.add(uniqueId.charAt(i));
        }
        return convertbase62Tobase10Id(base62Number);
    }

    private Long convertbase62Tobase10Id(List<Character> ids) {
        long id = 0L;
        int exp = ids.size() - 1;
        for (int i = 0; i < ids.size(); ++i, --exp) {
            int base10 = charToIndexTable.get(ids.get(i));
            id += (base10 * Math.pow(62.0, exp));
        }
        return id;
    }
}
