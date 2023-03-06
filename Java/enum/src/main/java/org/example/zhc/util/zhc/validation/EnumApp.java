package org.example.zhc.util.zhc.validation;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class EnumApp {
    @Test
    public void enumTest(){
//        assert false;
//        LogicType[] values = LogicType.values();
//        for(LogicType type: values){
//            System.out.println("type.name: "+type.name());
//            System.out.println("type.ordinal: "+type.ordinal());
//            System.out.println("type.type: "+type.type());
//        }
        try {
            ttt();
        }catch (Exception e){
            System.out.println("fff");
        }
    }

    public void ttt(){
        try {
            throw new RuntimeException("ff");
        }finally {
            System.out.println("rrr");
        }
    }
    @Test
    public void versionTest(){
        System.out.println(convertVersionToInt32("0.0.1"));
        System.out.println(convertVersionToInt32("1.0.1"));
        System.out.println(convertVersionToInt32("1.1.0"));
        System.out.println(convertVersionToInt32("2.0.0"));
    }
    /**
     * This algorithm originally written by Patrick Muff (dislick) for JS ES6.
     * https://gist.github.com/dislick/914e67444f8f71df3900bd77ccec6091
     */

    /**
     * Convert a semantic versioning string into a 32-bit integer.
     *
     * Make sure the input string is compatible with the standard found
     * at semver.org. Since this only uses 10-bit per major/minor/patch version,
     * the highest possible SemVer string would be 1023.1023.1023.
     * @param version {String} SemVer version string
     * @return {Integer} numeric 32-bit integer version
     *
     * Example: convertVersionToInt32('2.5.3'); // 3150850
     */
    private Integer convertVersionToInt32(String version) {
        // Split a given version string into three parts.
        List < Integer > versions = Arrays.stream(version.split("\\."))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        // Check if we got exactly three parts, otherwise throw an error.
        // TODO: change error
        if (versions.size() != 3)
            throw new Error("Received invalid version string");

        // Make sure that no part is larger than 1023 or else it won't fit into a 32-bit integer.
        if (versions.stream().anyMatch(ver -> ver >= 1024))
            throw new Error("Version string invalid, some parts is bigger than 1024.");

        // Let's create a new Integer which we will return later on
        Integer numericVersion = 0;

        // Shift all parts either 0, 10 or 20 bits to the left.
        for (Integer i = 0; i < 3; i++)
            numericVersion |= versions.get(i) << (2-i) * 10;

        return numericVersion;
    }

//    /**
//     * Converts a 32-bit integer into a semantic versioning (SemVer) compatible string.
//     * @param  number {Integer} numeric 32-bit integer version
//     * @return {String} SemVer version string
//     *
//     * Example: convertInt32VersionToString(3150850); // '2.5.3'
//     */
//    private String convertInt32VersionToString(Integer number) {
//        // Works by shifting the numeric version to the right and then masking it
//        // with 0b1111111111 (or 1023 in decimal).
//        return (number & 1023) + "." + (number >> 10 & 1023) + "." + (number >> 20 & 1023);
//    };
}
