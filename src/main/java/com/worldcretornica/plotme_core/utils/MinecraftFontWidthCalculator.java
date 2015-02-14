package com.worldcretornica.plotme_core.utils;

/**
 * DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE Version 2, December 2004
 * Copyright (C) 2004 Sam Hocevar <sam@hocevar.net>
 * <p/>
 * Everyone is permitted to copy and distribute verbatim or modified copies of
 * this license document, and changing it is allowed as long as the name is
 * changed.
 * <p/>
 * DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE TERMS AND CONDITIONS FOR COPYING,
 * DISTRIBUTION AND MODIFICATION
 * <p/>
 * 0. You just DO WHAT THE FUCK YOU WANT TO.
 */
public class MinecraftFontWidthCalculator {

    private static final String charWidthIndexIndex = " !\"#$%"
            + "&'()*+"
            + ",-./"
            + "0123456789"
            + ":;<=>?@"
            + "ABCDEF"
            + "GHIJKL"
            + "MNOPQR"
            + "STUVWX"
            + "YZ"
            + "[\\]^_'abcdefghijklmnopqrstuvwxyz{|}~⌂ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»";
    private static final int[] charWidths = {16, 6, 18, 15, 18, 18,
            18, 18, 20, 20, 20, 24,
            8, 24, 8, 24,
            24, 24, 24, 24, 24, 24, 24, 24, 24, 24,
            8, 8, 20, 24, 20, 24, 28,
            24, 24, 24, 24, 24, 24,
            24, 24, 16, 24, 24, 24,
            24, 24, 24, 24, 24, 24,
            24, 24, 24, 24, 24, 24,
            24, 24,
            18, 24, 16, 24, 24, 12, 24, 24, 24, 24, 24,
            20, 24, 24, 8, 24, 20, 12, 24, 24, 24, 24, 24, 24, 24, 16, 24, 24, 24, 24,
            24, 24, 20, 8, 20, 28, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 16, 24, 12, 24, 24, 24, 24, 24, 24, 24,
            24, 24, 24, 24, 24, 24, 24, 24, 24, 16, 24, 24, 12, 24, 24, 24, 24, 24, 24, 24, 28,
            24, 24, 24, 8, 24, 24, 32, 36, 36, 24, 24, 24, 32, 32, 24, 32, 32, 32, 32, 32, 24, 24, 36, 36, 36, 36, 36, 36,
            36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 24, 36,
            36, 36, 20, 36, 36, 32, 28, 28, 32, 28, 32, 32, 32, 28, 32, 32, 28, 36, 36, 24, 28, 28, 28, 28, 28, 36, 24, 28,
            32, 28, 24, 24, 36, 28, 24, 28, 4};

    public static int getStringWidth(String s) {
        int i = 0;
        if (s != null) {
            for (int j = 0; j < s.length(); j++) {
                i += getCharWidth(s.charAt(j));
            }
        }
        return i;
    }

    public static int getCharWidth(char c) {
        int k = charWidthIndexIndex.indexOf(c);
        if (c != '\247' && k >= 0) {
            return charWidths[k];
        }
        return 0;
    }
}
