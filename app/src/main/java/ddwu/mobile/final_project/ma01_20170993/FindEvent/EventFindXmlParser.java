package ddwu.mobile.final_project.ma01_20170993.FindEvent;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;


public class EventFindXmlParser {

    public enum TagType { NONE, ADDR1, FIRSTIMAGE, MAPX, MAPY, TEL, TITLE, EVENTSTARTDATE, EVENTENDDATE, CONTENTID, CONTENTTYPEID };

    final static String TAG_ITEM = "item";
    final static String TAG_ADDR1 = "addr1";
    final static String TAG_FIRSTIMAGE = "firstimage";
    final static String TAG_MAPX = "mapx";
    final static String TAG_MAPY = "mapy";
    final static String TAG_TEL = "tel";
    final static String TAG_TITLE = "title";
    final static String TAG_CONTENTID = "contentid";
    final static String TAG_EVENTSTARTDATE = "eventstartdate";
    final static String TAG_EVENTENDDATE = "eventenddate";
    final static String TAG_CONTENTTYPEID = "contenttypeid";

    public EventFindXmlParser() {
    }

    // 검색결과에 해당하는(어린이집 정보를 담은) xml을 받아와 파싱하는 메소드
    public ArrayList<EventDto> parse(String xml) {

        ArrayList<EventDto> resultList = new ArrayList();
        EventDto dto = null;

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
                            dto = new EventDto();
                        } else if (parser.getName().equals(TAG_ADDR1)) {
                            if (dto != null) tagType = TagType.ADDR1;
                        } else if (parser.getName().equals(TAG_FIRSTIMAGE)) {
                            if (dto != null) tagType = TagType.FIRSTIMAGE;
                        } else if (parser.getName().equals(TAG_MAPX)) {
                            if (dto != null) tagType = TagType.MAPX;
                        } else if (parser.getName().equals(TAG_MAPY)) {
                            if (dto != null) tagType = TagType.MAPY;
                        } else if (parser.getName().equals(TAG_TEL)) {
                            if (dto != null) tagType = TagType.TEL;
                        } else if (parser.getName().equals(TAG_TITLE)) {
                            if (dto != null) tagType = TagType.TITLE;
                        } else if (parser.getName().equals(TAG_EVENTENDDATE)) {
                            if (dto != null) tagType = TagType.EVENTENDDATE;
                        } else if (parser.getName().equals(TAG_EVENTSTARTDATE)) {
                            if (dto != null) tagType = TagType.EVENTSTARTDATE;
                        } else if (parser.getName().equals(TAG_CONTENTID)) {
                            if (dto != null) tagType = TagType.CONTENTID;
                        } else if (parser.getName().equals(TAG_CONTENTTYPEID)) {
                            if (dto != null) tagType = TagType.CONTENTTYPEID;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(TAG_ITEM)) {
                            resultList.add(dto);
                            dto = null;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch(tagType) {
                            case ADDR1:
                                dto.setAddr1(parser.getText());
                                break;
                            case FIRSTIMAGE:
                                dto.setFirstimage(parser.getText());
                                break;
                            case MAPX:
                                dto.setMapX(Double.parseDouble(parser.getText()));
                                break;
                            case MAPY:
                                dto.setMapY(Double.parseDouble(parser.getText()));
                                break;
                            case TEL:
                                dto.setTel(parser.getText());
                                break;
                            case TITLE:
                                dto.setTitle(parser.getText());
                                break;
                            case EVENTENDDATE:
                                dto.setEventenddate(parser.getText());
                                break;
                            case EVENTSTARTDATE:
                                dto.setEventstartdate(parser.getText());
                                break;
                            case CONTENTID:
                                dto.setContentid(parser.getText());
                                break;
                            case CONTENTTYPEID:
                                dto.setContentTypeId(parser.getText());
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

        return resultList;
    }
}
