package fr.ocr.utiles;


import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import java.util.List;

public class MesOptions {


    @Option(name = "-d", aliases = {"--develo"}, usage = "-d | --develo  :   mode developpeur")
    private boolean modeDebug;


    @Argument
    private List<String> argument;

    public List<String> getArgument() {
        return argument;
    }

    public boolean isModeDebug() {
        return modeDebug;
    }

}