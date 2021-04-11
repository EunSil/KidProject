package ddwu.mobile.final_project.ma01_20170993.FindKindergarden;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;


public class KindergardenFindXmlParser {

    public enum TagType { NONE, NAME, ADDRESS, FIXEDNUMBER, PRESENTNUMBER, DISTRICT, COTCONTSID, LATITUDE, LONGITUDE };

    final static String TAG_ITEM = "row";
    final static String TAG_NAME = "COT_CONTS_NAME";
    final static String TAG_ADDRESS = "COT_ADDR_FULL_NEW";
    final static String TAG_FIXEDNUMBER = "COT_VALUE_03";
    final static String TAG_PRESENTNUMBER = "COT_VALUE_04";
    final static String TAG_DISTRICT = "COT_GU_NAME";
    final static String TAG_COTCONTSID = "COT_CONTS_ID";
    final static String TAG_LATITUDE = "COT_COORD_Y";
    final static String TAG_LONGITUDE = "COT_COORD_X";

    public KindergardenFindXmlParser() {
    }

    // 검색결과에 해당하는(어린이집 정보를 담은) xml을 받아와 파싱하는 메소드
    public ArrayList<KindergardenDto> parse(String xml) {

        ArrayList<KindergardenDto> resultList = new ArrayList();
        KindergardenDto dto = null;

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
                            dto = new KindergardenDto();
                        } else if (parser.getName().equals(TAG_NAME)) {
                            if (dto != null) tagType = TagType.NAME;
                        } else if (parser.getName().equals(TAG_ADDRESS)) {
                            if (dto != null) tagType = TagType.ADDRESS;
                        } else if (parser.getName().equals(TAG_FIXEDNUMBER)) {
                            if (dto != null) tagType = TagType.FIXEDNUMBER;
                        } else if (parser.getName().equals(TAG_PRESENTNUMBER)) {
                            if (dto != null) tagType = TagType.PRESENTNUMBER;
                        } else if (parser.getName().equals(TAG_DISTRICT)) {
                            if (dto != null) tagType = TagType.DISTRICT;
                        } else if (parser.getName().equals(TAG_COTCONTSID)) {
                            if (dto != null) tagType = TagType.COTCONTSID;
                        } else if (parser.getName().equals(TAG_LATITUDE)) {
                            if (dto != null) tagType = TagType.LATITUDE;
                        } else if (parser.getName().equals(TAG_LONGITUDE)) {
                            if (dto != null) tagType = TagType.LONGITUDE;
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
                            case NAME:
                                dto.setName(parser.getText());
                                break;
                            case ADDRESS:
                                dto.setAddress(parser.getText());
                                break;
                            case FIXEDNUMBER:
                                dto.setFixedNumber(parser.getText());
                                break;
                            case PRESENTNUMBER:
                                dto.setPresentNumber(parser.getText());
                                break;
                            case DISTRICT:
                                dto.setDistrict(parser.getText());
                                break;
                            case COTCONTSID:
                                dto.setStcode(parser.getText());
                                break;
                            case LATITUDE:
                                dto.setLatitude(Double.parseDouble(parser.getText()));
                                break;
                            case LONGITUDE:
                                dto.setLongitude(Double.parseDouble(parser.getText()));
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
