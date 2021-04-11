package ddwu.mobile.final_project.ma01_20170993.FindEvent;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;


public class EventDetailXmlParser {

    public enum TagType { NONE, ADDR, TIME, HOME, AGE };

    final static String TAG_ITEM = "item";
    final static String TAG_ADDR = "eventplace";
    final static String TAG_TIME = "playtime";
    final static String TAG_HOME = "eventhomepage";
    final static String TAG_AGE = "agelimit";

    public EventDetailXmlParser() {
    }

    public EventDetailDto parse(String xml) {

        EventDetailDto dto = null;

        TagType tagType = TagType.NONE;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals(TAG_ITEM)) {
                            dto = new EventDetailDto();
                        } else if (parser.getName().equals(TAG_ADDR)) {
                            if (dto != null) tagType = TagType.ADDR;
                        } else if (parser.getName().equals(TAG_AGE)) {
                            if (dto != null) tagType = TagType.AGE;
                        } else if (parser.getName().equals(TAG_HOME)) {
                            if (dto != null) tagType = TagType.HOME;
                        } else if (parser.getName().equals(TAG_TIME)) {
                            if (dto != null) tagType = TagType.TIME;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        switch(tagType) {
                            case ADDR:
                                dto.setPlace(parser.getText());
                                break;
                            case AGE:
                                dto.setAge(parser.getText());
                                break;
                            case HOME:
                                dto.setHome(RemoveHTMLTag(parser.getText()));
                                break;
                            case TIME:
                                dto.setTime(RemoveHTMLTag(parser.getText()));
                                break;
                        }
                        tagType = TagType.NONE;
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dto;
    }

    public String RemoveHTMLTag(String changeStr){
        if(changeStr != null && !changeStr.equals("")){
            changeStr = changeStr.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
        }else{
            changeStr = "";
        }
        return changeStr;
    }

}
