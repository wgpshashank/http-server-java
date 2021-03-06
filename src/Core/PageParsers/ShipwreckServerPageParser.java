package Core.PageParsers;

import Core.ServerRunner;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShipwreckServerPageParser implements SingleLineParser
{
    private ShipwreckServerPageParser() {}

    public static ShipwreckServerPageParser create()
    {
        return new ShipwreckServerPageParser();
    }

    public String parseSingleLine(String line, List<AbstractMap.SimpleEntry<String, String>> values)
    {
        if(!hasParseableSyntax(line))
            return line;

        boolean continueLookingSyntaxMatches = true;
        while(continueLookingSyntaxMatches)
        {
            Pattern regexPattern = Pattern.compile("<\\^=(.*?)\\^>");
            Matcher regexMatcher = regexPattern.matcher(line);
            if(!regexMatcher.find())
            {
                continueLookingSyntaxMatches = false;
                continue;
            }
            line = replaceFirstMatchForActualValues(regexMatcher, values);
        }

        return line;
    }

    private String replaceFirstMatchForActualValues(Matcher regexMatcher, List<AbstractMap.SimpleEntry<String, String>> values)
    {
        String lookupKey = regexMatcher.group(0).replace("<^=", "").replace("^>", "").trim();
        String value = getValueFrom(values, lookupKey);
        return regexMatcher.replaceFirst(value);
    }

    private String getValueFrom(List<AbstractMap.SimpleEntry<String, String>> values, String key)
    {
        Iterator<AbstractMap.SimpleEntry<String, String>> iterator = values.iterator();
        while(iterator.hasNext())
        {
            AbstractMap.SimpleEntry<String, String> entry = iterator.next();
            if(entry.getKey().toLowerCase().equals(key.toLowerCase()))
                return entry.getValue();
        }

        return "";
    }

    private boolean hasParseableSyntax(String line)
    {
        return line.contains("<^=") && line.contains("^>");
    }
}
