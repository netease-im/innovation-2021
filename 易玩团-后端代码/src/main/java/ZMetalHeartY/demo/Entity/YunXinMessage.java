package ZMetalHeartY.demo.Entity;

import lombok.Data;

@Data
public class YunXinMessage {

    /**
     * 发送用户ID
     */
    private String from;

    /**
     * 用来决定to的类型 0表示用户ID 1表示群ID
     */
    private Integer ope;

    /**
     * 接收用户ID
     */
    private String to;

    /**
     * 0文本 1图片 2语音 3视频 4地理位置信息 6文件 10提示消息 100自定义
     */
    private Integer type;

    /**
     * 内容
     * 参考：https://doc.yunxin.163.com/docs/TM5MzM5Njk/DEwMTE3NzQ?platformId=60353#%E6%B6%88%E6%81%AF%E6%A0%BC%E5%BC%8F%E7%A4%BA%E4%BE%8B
     */
    private String body;

    /**
     * 消息描述文本
     */
    private String msgDesc;

    /**
     * 是否对接易盾
     */
    private Boolean antispam;

    /**
     * 自定义反垃圾信息内容 json格式例如{"type":1,"data":"custom content"}
     */
    private String antispamCustom;

    /**
     * 发消息时特殊指定的行为选项,JSON格式，可用于指定消息的漫游，存云端历史，发送方多端同步，推送，消息抄送等特殊行为;option中字段不填时表示默认值
     */
    private String option;

    /**
     * 推送文案,最长500个字符。
     */
    private String pushcontent;

    /**
     * 必须是JSON,不能超过2k字符。该参数与APNs推送的payload含义不同。具体参见 https://faq.yunxin.163.com/kb/main/#/item/KB0291
     */
    private String payload;

    /**
     * 开发者扩展字段，长度限制1024字符
     */
    private String ext;

    /**
     * 发送群消息时的强推用户列表（云信demo中用于承载被@的成员），格式为JSONArray，如["accid1","accid2"]。若forcepushall为true，则forcepushlist为除发送者外的所有有效群成员
     */
    private String forcepushlist;

    /**
     * 发送群消息时，针对强推列表forcepushlist中的用户，强制推送的内容
     */
    private String forcepushcontent;

    /**
     * 发送群消息时，强推列表是否为群里除发送者外的所有有效成员，true或false，默认为false
     */
    private String forcepushall;

    /**
     * 可选，反垃圾业务ID，实现“单条消息配置对应反垃圾”
     */
    private String bid;

    /**
     * 可选，单条消息是否使用易盾反垃圾，可选值为0。
     */
    private Integer useYidun;

    /**
     * 可选 反作弊
     */
    private String yidunAntiCheating;


    /**
     * 可选 群消息是否需要已读业务 0不需要 1需要
     */
    private Integer markRead;

    /**
     * 是否为好友关系才发送消息，默认否
     */
    private Boolean checkFriend;


    /**
     * 自定义消息子类型，大于0
     */
    private String subType;

    /**
     * 发送方是否无感知。0-有感知，1-无感知。若无感知，则消息发送者无该消息的多端、漫游、历史记录等。
     */
    private Integer msgSenderNoSense;


    /**
     * 接受方是否无感知。0-有感知，1-无感知。若无感知，则消息接收者者无该消息的多端、漫游、历史记录等
     */
    private Integer msgReceiverNoSense;

    /**
     * 所属环境，根据env可以配置不同的抄送地址
     */
    private String env;

}
