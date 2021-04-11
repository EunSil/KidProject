package ddwu.mobile.final_project.ma01_20170993.FindKindergarden;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;


public class DetailKindergardenXmlParser {

    public enum TagType { NONE, TYPE, OPENDATE, PHONENUMBER, HOMEPAGE, SPEC, ISCAR, ROOMCNT, ROOMSIZE, PLAYCNT, CCTVCNT };

    final static String TAG_ITEM = "item";
    final static String TAG_TYPE = "crtypename";
    final static String TAG_OPENDATE = "crcnfmdt";
    final static String TAG_PHONENUMBER = "crtelno";
    final static String TAG_HOMEPAGE = "crhome";
    final static String TAG_SPEC = "crspec";
    final static String TAG_ISCAR = "crcargbname";
    final static String TAG_ROOMCNT = "nrtrroomcnt";
    final static String TAG_ROOMSIZE = "nrtrroomsize";
    final static String TAG_PLAYCNT = "plgrdco";
    final static String TAG_CCTVCNT = "cctvinstlcnt";

    public DetailKindergardenXmlParser() {
    }

    // 해당 어린이집과 같은 구에 위치한 문화시설 정보를 받아와 파싱하는 메소드
    public DetailKindergardenDto parse(String xml) {
        DetailKindergardenDto dto = null;

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
                            dto = new DetailKindergardenDto();
                        } else if (parser.getName().equals(TAG_TYPE)) {
                            if (dto != null) tagType = TagType.TYPE;
                        } else if (parser.getName().equals(TAG_OPENDATE)) {
                            if (dto != null) tagType = TagType.OPENDATE;
                        } else if (parser.getName().equals(TAG_PHONENUMBER)) {
                            if (dto != null) tagType = TagType.PHONENUMBER;
                        } else if (parser.getName().equals(TAG_HOMEPAGE)) {
                            if (dto != null) tagType = TagType.HOMEPAGE;
                        } else if (parser.getName().equals(TAG_SPEC)) {
                            if (dto != null) tagType = TagType.SPEC;
                        } else if (parser.getName().equals(TAG_ISCAR)) {
                            if (dto != null) tagType = TagType.ISCAR;
                        } else if (parser.getName().equals(TAG_ROOMCNT)) {
                            if (dto != null) tagType = TagType.ROOMCNT;
                        } else if (parser.getName().equals(TAG_ROOMSIZE)) {
                            if (dto != null) tagType = TagType.ROOMSIZE;
                        } else if (parser.getName().equals(TAG_PLAYCNT)) {
                            if (dto != null) tagType = TagType.PLAYCNT;
                        } else if (parser.getName().equals(TAG_CCTVCNT)) {
                            if (dto != null) tagType = TagType.CCTVCNT;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        switch(tagType) {
                            case TYPE:
                                dto.setTypeName(parser.getText());
                                break;
                            case OPENDATE:
                                dto.setOpenDate(parser.getText());
                                break;
                            case PHONENUMBER:
                                dto.setPhoneNumber(parser.getText());
                                break;
                            case HOMEPAGE:
                                dto.setHomePage(parser.getText());
                                break;
                            case SPEC:
                                dto.setSpec(parser.getText());
                                break;
                            case ISCAR:
                                dto.setIsCar(parser.getText());
                                break;
                            case ROOMCNT:
                                dto.setCareRoomCnt(parser.getText());
                                break;
                            case ROOMSIZE:
                                dto.setCareRoomSize(parser.getText());
                                break;
                            case PLAYCNT:
                                dto.setPlayCnt(parser.getText());
                                break;
                            case CCTVCNT:
                                dto.setCctvCnt(parser.getText());
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
}
