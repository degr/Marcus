package org.forweb.marcus.sorter;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by rsmirnou on 7/31/2015. 59
 */
public class StringSorter {


    public static final int FULL_COMPATIBLE = 15;
    public static final int PARTIAL_COMPATIBLE = 10;
    public static final int SPLITTED_COMPATIBLE = 5;
    public static final int FULL_INCOMPATIBLE = 0;

    public static final int CASE_COMPATIBLE = 5;
    public static final int IGNORE_FIRST_LETTER_CASE_COMPATIBLE = 3;
    public static final int IGNORE_CASE_COMPATIBLE = 1;

    public static final int DIRECT_ORDER = 5;
    public static final int REVERSE_ORDER = 3;
    public static final int RANDOM_ORDER = 1;

    /**
     * for split by ['camelCase', ' ', '-', '+', '*', '\', '/', '.', ',', '!', '?', '~', '@', '#', '$', '%', '^', '&',
     * '(', '*', ')', '<', '>', '|', '[', ']', '{', '}']
     */
    public static final Pattern SPLITTER = Pattern.compile("(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])|([ \\-+*\\/=\\\\_.,!?~@#$%^&()<>|\\]\\[\\}\\{]{1,})");

    public static class Dto{
        public String str;
        public Integer pos;
    }

    public static List<String> findCompatible(Collection<String> collection, String search) {
        if(collection == null || collection.isEmpty()) {
            return null;
        }
        String[] parts = SPLITTER.split(search, 0);

        List<Integer> positions = collection.stream()
                .map(v -> defineCompatible(v, search, parts))
                .collect(Collectors.toList());

        List<Dto> out = new ArrayList<>();

        int i = 0;
        for(String item : collection) {
            Dto dto = new Dto();
            dto.str = item;
            dto.pos = positions.get(i);
            out.add(dto);
            i++;
        }
        return out.stream().sorted((v1, v2) -> v2.pos - v1.pos).map(v -> v.str).collect(Collectors.toList());
    }

    private static Integer defineCompatible(String item, String search, String[] parts) {

        if(item.equals(search)) {
            return (FULL_COMPATIBLE + CASE_COMPATIBLE + DIRECT_ORDER) * parts.length;
        }

        String icItem = item.toLowerCase();
        String icSearch = search.toLowerCase();

        if(icItem.equals(icSearch)) {
            if(StringUtils.capitalize(icItem).equals(StringUtils.capitalize(icSearch))) {
                return (FULL_COMPATIBLE + IGNORE_FIRST_LETTER_CASE_COMPATIBLE + DIRECT_ORDER) * parts.length;
            } else {
                return (FULL_COMPATIBLE + IGNORE_CASE_COMPATIBLE + DIRECT_ORDER) * parts.length;
            }
        }
        if(item.contains(search)){
            return (PARTIAL_COMPATIBLE + CASE_COMPATIBLE + DIRECT_ORDER) * parts.length;
        }

        if(icItem.contains(icSearch)) {
            if(StringUtils.capitalize(icItem).equals(StringUtils.capitalize(icSearch))) {
                return (PARTIAL_COMPATIBLE + IGNORE_FIRST_LETTER_CASE_COMPATIBLE + DIRECT_ORDER) * parts.length;
            } else {
                return (PARTIAL_COMPATIBLE + IGNORE_CASE_COMPATIBLE + DIRECT_ORDER) * parts.length;
            }
        }
        String reversedSearch = "";
        for(int i = parts.length; i > 0; i--) {
            reversedSearch += i == parts.length ? parts[i-1].toLowerCase() : parts[i-1];
        }
        if(reversedSearch.equals(item)) {
            return (FULL_COMPATIBLE + CASE_COMPATIBLE + REVERSE_ORDER) * parts.length;
        }
        if(item.contains(reversedSearch)){
            return (PARTIAL_COMPATIBLE + CASE_COMPATIBLE + REVERSE_ORDER) * parts.length;
        }
        if(reversedSearch.contains(item)){
            return (SPLITTED_COMPATIBLE + CASE_COMPATIBLE + REVERSE_ORDER) * parts.length;
        }

        class SubString{
            boolean isIgnoreCase = false;
            boolean isPresent = false;
            int position;
            int length;
        }
        List<SubString> partsPositions = new ArrayList<>(parts.length);
        boolean allPresent = true;
        for (String part : parts) {
            SubString subString = new SubString();
            Integer position = item.indexOf(part);
            if (position == -1) {
                position = icItem.indexOf(part.toLowerCase());
                if (position > -1) {
                    subString.isPresent = true;
                    subString.isIgnoreCase = true;
                }
            } else {
                subString.isPresent = true;
            }
            if (subString.isPresent) {
                subString.position = position;
                subString.length = part.length();
            } else if(allPresent) {
                allPresent = false;
            }
            partsPositions.add(subString);
        }
        int out = FULL_INCOMPATIBLE;
        int lastPosition = 0;
        for (SubString subString : partsPositions) {
            if(subString.isPresent) {
                out += SPLITTED_COMPATIBLE
                        + (lastPosition == subString.position ? DIRECT_ORDER : RANDOM_ORDER)
                        + (subString.isIgnoreCase ? IGNORE_CASE_COMPATIBLE : CASE_COMPATIBLE);
                lastPosition = subString.position + subString.length;
            }
        }
        return out;
    }


}
