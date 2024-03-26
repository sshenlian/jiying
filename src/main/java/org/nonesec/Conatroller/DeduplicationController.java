package org.nonesec.Conatroller;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class DeduplicationController {
    public String GetDeduplication(String[] DeduplicationText){

        return Arrays.stream(DeduplicationText).distinct().collect(Collectors.joining("\n"));

    }

}
