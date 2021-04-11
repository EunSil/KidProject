package ddwu.mobile.final_project.ma01_20170993.FindEvent;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;


public class EventXmlParser {

    public enum TagType { NONE, CNT};

    final static String TAG_CNT = "totalCnt";

    public EventXmlParser() {
    }

    // 검색결과에 해당하는(어린이집 정보를 담은) xml을 받아와 파싱하는 메소드
    public int parse(String xml) {

        int result = 0;

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
                        if (parser.getName().equals(TAG_CNT)) {
                            tagType = TagType.CNT;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        switch(tagType) {
                            case CNT:
                                result = Integer.parseInt(parser.getText());
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

        return result;
    }
}
