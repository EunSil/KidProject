package ddwu.mobile.final_project.ma01_20170993.RankBook;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;


public class NaverBookXmlParser {

    public enum TagType { NONE, TITLE, AUTHOR, IMAGE, LINK };

    final static String TAG_ITEM = "item";
    final static String TAG_TITLE = "title";
    final static String TAG_AUTHOR = "author";
    final static String TAG_IMAGE = "coverSmallUrl";
    final static String TAG_LINK = "additionalLink";

    public NaverBookXmlParser() {
    }

    public ArrayList<NaverBookDto> parse(String xml) {

        ArrayList<NaverBookDto> resultList = new ArrayList();
        NaverBookDto dto = null;

        TagType tagType = TagType.NONE;

        try {
            int count = 0;
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
                            dto = new NaverBookDto();
                        } else if (parser.getName().equals(TAG_TITLE)) {
                            if (dto != null) tagType = TagType.TITLE;
                        } else if (parser.getName().equals(TAG_AUTHOR)) {
                            if (dto != null) tagType = TagType.AUTHOR;
                        } else if (parser.getName().equals(TAG_IMAGE)) {
                            if (dto != null) tagType = TagType.IMAGE;
                        } else if (parser.getName().equals(TAG_LINK)) {
                            if (dto != null) tagType = TagType.LINK;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(TAG_ITEM)) {
                            count ++;
                            dto.setRank(Integer.toString(count));
                            resultList.add(dto);
                            dto = null;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch(tagType) {
                            case TITLE:
                                dto.setTitle(parser.getText());
                                break;
                            case AUTHOR:
                                dto.setAuthor(parser.getText());
                                break;
                            case IMAGE:
                                dto.setImageLink(parser.getText());
                                break;
                            case LINK:
                                dto.setLink(parser.getText());
                                break;
                        }
                        tagType = TagType.NONE;
                        break;
                }
                eventType = parser.next();
                if(count == 10)
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }
}
