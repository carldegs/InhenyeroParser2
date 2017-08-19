package com.inhenyero;

import java.io.File;

/**
 * Created by Carl on 19 Aug 2017.
 */
public class Constants {
    public static final String START_TEXT =
            "      .sSSSSs.                  SsSSs.    .sSSSSs.   \n" +
            "SSSSS `SSSS SSSs. .sSSS  SSSSS    SSSSs   SSSSSSSSSs.\n" +
            "S SSS       SSSSS S SSS SSSSS     S SSS   S SSS SSSS'\n" +
            "S  SS .sSSSsSSSS' S  SS SSSSS     S  SS   S  SS      \n" +
            "S..SS S..SS       S..SSsSSSSS     S..SS   S...SsSSSa.\n" +
            "S:::S S:::S SSSs. S:::S SSSSS     S:::S   S:::S SSSSS\n" +
            "S;;;S S;;;S SSSSS S;;;S  SSSSS    S;;;S   S;;;S SSSSS\n" +
            "S%%%S S%%%S SSSSS S%%%S  SSSSS    S%%%S   S%%%S SSSSS\n" +
            "SSSSS SSSSSsSSSSS SSSSS   SSSSS SsSSSSSsS `:;SSsSS;:'\n" +
            "                                                     \n";

    public static final String PUBLIC_DIRECTORY_PATH = new File("public/").getAbsolutePath();
    public static final String MASTERDATA_PATH = new File("public/masterdata.csv").getAbsolutePath();
}
