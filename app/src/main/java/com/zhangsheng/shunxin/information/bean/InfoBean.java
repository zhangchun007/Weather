package com.zhangsheng.shunxin.information.bean;


import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @Author:liupengbing
 * @Data: 2020/5/23 10:38 AM
 * @Email:aliupengbing@163.com
 */
public class InfoBean {
    public static final int COVER_MODE_AD = 100;
    public static final int COVER_MODE_VIDEO = 200;

    public static final int AD_NOT_LOAD = 0;
    public static final int AD_LOADING = 1;
    public static final int AD_LOADED = 2;

    /**
     * data : [{"abstract":"1.脱掉内衣胸没那么大2.无限循环FLAG+\u201c真香\u201d3.男生认为无聊的细节，女生往往很重视4.喜欢说反话5.不喜欢听大道理其实，真爱你的人是不会因为这些嫌弃你的","article_type":0,"article_url":"http://open-hl.toutiao.com/a6801328491112432139/?utm_campaign=open&utm_medium=webview&utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50&item_id=6801328491112432139","ban_comment":0,"behot_time":1590201421,"bury_count":0,"cell_type":0,"comment_count":315,"comment_url":"http://open.toutiao.com/a6801328491112432139/comment/?utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50","cover_image_list":[{"height":540,"uri":"img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586","url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:960:540.webp?from=feed","url_list":[{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:960:540.webp?from=feed"}],"width":960}],"cover_mode":1,"digg_count":711,"filter_words":[{"id":"8:0","is_selected":false,"name":"看过了"},{"id":"9:1","is_selected":false,"name":"内容太水"},{"id":"5:2163777269","is_selected":false,"name":"拉黑作者:就要花里胡哨"},{"id":"6:734672582","is_selected":false,"name":"不想看:交个朋友吧"}],"gallary_image_count":5,"group_id":6801328491112432139,"group_source":2,"has_gallery":false,"has_video":false,"image_list":[],"is_stick":false,"item_id":6801328491112432139,"label":"","large_image_list":[{"height":540,"uri":"img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586","url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:960:540.webp?from=feed","url_list":[{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:960:540.webp?from=feed"}],"width":960}],"middle_image":{"height":196,"uri":"img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586","url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},"publish_time":1583557690,"share_count":192,"share_url":"http://open.toutiao.com/a6801328491112432139/?app=news_article&utm_source=zx_rywnl_api&utm_medium=webview&utm_campaign=open&is_hit_share_recommend=0&label=share&a_t=30159878878316673751415307019620&req_id=202005231037010100220750281C3C831D","source":"就要花里胡哨","tag":"news","tip":0,"title":"男朋友眼中的你，哈哈哈","url":"http://open-hl.toutiao.com/a6801328491112432139/?utm_campaign=open&utm_medium=webview&utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50&item_id=6801328491112432139","user_info":{"avatar_url":"http://sf6-ttcdn-tos.pstatp.com/img/pgc-image/719bfb75c812480d9ebf23009317ae0d~120x256.image","description":"这个人很懒，什么都没留下。","follow":false,"follower_count":0,"home_page":"http://open.toutiao.com/u63675336389/?utm_source=zx_rywnl_api&label=homepage&media_id=1659441453405198","media_id":1659441453405198,"name":"就要花里胡哨","user_id":63675336389,"user_verified":false}},{"abstract":"今天凌晨，有大量三星用户反应，自己的手机自动更新系统，导致系统直接崩溃，无法开机使用！有的出现乱码，有的是黑屏，有的则是无限重启，总之手机无法正常使用！目测是系统有重大错误！","article_type":0,"article_url":"http://open-hl.toutiao.com/a6829769856821232140/?utm_campaign=open&utm_medium=webview&utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50&item_id=6829769856821232140","ban_comment":0,"behot_time":1590201326,"bury_count":0,"cell_type":0,"comment_count":45,"comment_url":"http://open.toutiao.com/a6829769856821232140/comment/?utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50","cover_image_list":[{"height":196,"uri":"img/dfic-imagehandler/dcb48687-ca43-43c8-981d-b7a0671ec8d9","url":"http://p3-tt-ipv6.byteimg.com/img/dfic-imagehandler/dcb48687-ca43-43c8-981d-b7a0671ec8d9~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p3-tt-ipv6.byteimg.com/img/dfic-imagehandler/dcb48687-ca43-43c8-981d-b7a0671ec8d9~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p3-tt-ipv6.byteimg.com/img/dfic-imagehandler/dcb48687-ca43-43c8-981d-b7a0671ec8d9~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p3-tt-ipv6.byteimg.com/img/dfic-imagehandler/dcb48687-ca43-43c8-981d-b7a0671ec8d9~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},{"height":196,"uri":"img/dfic-imagehandler/4cbc38dc-b3c8-459a-80e7-041b25b7e610","url":"http://p9-tt-ipv6.byteimg.com/img/dfic-imagehandler/4cbc38dc-b3c8-459a-80e7-041b25b7e610~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p9-tt-ipv6.byteimg.com/img/dfic-imagehandler/4cbc38dc-b3c8-459a-80e7-041b25b7e610~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p9-tt-ipv6.byteimg.com/img/dfic-imagehandler/4cbc38dc-b3c8-459a-80e7-041b25b7e610~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p9-tt-ipv6.byteimg.com/img/dfic-imagehandler/4cbc38dc-b3c8-459a-80e7-041b25b7e610~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},{"height":196,"uri":"img/dfic-imagehandler/32992226-eec6-41f6-8c44-8b9c73232037","url":"http://p26-tt.byteimg.com/img/dfic-imagehandler/32992226-eec6-41f6-8c44-8b9c73232037~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p26-tt.byteimg.com/img/dfic-imagehandler/32992226-eec6-41f6-8c44-8b9c73232037~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p26-tt.byteimg.com/img/dfic-imagehandler/32992226-eec6-41f6-8c44-8b9c73232037~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p26-tt.byteimg.com/img/dfic-imagehandler/32992226-eec6-41f6-8c44-8b9c73232037~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300}],"cover_mode":2,"digg_count":25,"filter_words":[{"id":"8:0","is_selected":false,"name":"看过了"},{"id":"9:1","is_selected":false,"name":"内容太水"},{"id":"5:919742110","is_selected":false,"name":"拉黑作者:简小白"},{"id":"2:11781250","is_selected":false,"name":"不想看:手机"},{"id":"6:19728","is_selected":false,"name":"不想看:三星"}],"gallary_image_count":7,"group_id":6829769856821232140,"group_source":2,"has_gallery":false,"has_video":false,"image_list":[{"height":196,"uri":"img/dfic-imagehandler/dcb48687-ca43-43c8-981d-b7a0671ec8d9","url":"http://p3-tt-ipv6.byteimg.com/img/dfic-imagehandler/dcb48687-ca43-43c8-981d-b7a0671ec8d9~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p3-tt-ipv6.byteimg.com/img/dfic-imagehandler/dcb48687-ca43-43c8-981d-b7a0671ec8d9~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p3-tt-ipv6.byteimg.com/img/dfic-imagehandler/dcb48687-ca43-43c8-981d-b7a0671ec8d9~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p3-tt-ipv6.byteimg.com/img/dfic-imagehandler/dcb48687-ca43-43c8-981d-b7a0671ec8d9~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},{"height":196,"uri":"img/dfic-imagehandler/4cbc38dc-b3c8-459a-80e7-041b25b7e610","url":"http://p9-tt-ipv6.byteimg.com/img/dfic-imagehandler/4cbc38dc-b3c8-459a-80e7-041b25b7e610~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p9-tt-ipv6.byteimg.com/img/dfic-imagehandler/4cbc38dc-b3c8-459a-80e7-041b25b7e610~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p9-tt-ipv6.byteimg.com/img/dfic-imagehandler/4cbc38dc-b3c8-459a-80e7-041b25b7e610~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p9-tt-ipv6.byteimg.com/img/dfic-imagehandler/4cbc38dc-b3c8-459a-80e7-041b25b7e610~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},{"height":196,"uri":"img/dfic-imagehandler/32992226-eec6-41f6-8c44-8b9c73232037","url":"http://p26-tt.byteimg.com/img/dfic-imagehandler/32992226-eec6-41f6-8c44-8b9c73232037~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p26-tt.byteimg.com/img/dfic-imagehandler/32992226-eec6-41f6-8c44-8b9c73232037~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p26-tt.byteimg.com/img/dfic-imagehandler/32992226-eec6-41f6-8c44-8b9c73232037~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p26-tt.byteimg.com/img/dfic-imagehandler/32992226-eec6-41f6-8c44-8b9c73232037~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300}],"is_stick":false,"item_id":6829769856821232140,"label":"","large_image_list":[],"middle_image":{"height":196,"uri":"img/dfic-imagehandler/dcb48687-ca43-43c8-981d-b7a0671ec8d9","url":"http://p1-tt-ipv6.byteimg.com/img/dfic-imagehandler/dcb48687-ca43-43c8-981d-b7a0671ec8d9~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p1-tt-ipv6.byteimg.com/img/dfic-imagehandler/dcb48687-ca43-43c8-981d-b7a0671ec8d9~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/dfic-imagehandler/dcb48687-ca43-43c8-981d-b7a0671ec8d9~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/dfic-imagehandler/dcb48687-ca43-43c8-981d-b7a0671ec8d9~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},"publish_time":1590180186,"share_count":97,"share_url":"http://open.toutiao.com/a6829769856821232140/?app=news_article&utm_source=zx_rywnl_api&utm_medium=webview&utm_campaign=open&is_hit_share_recommend=0&label=share&a_t=30159878878316673751415307019620&req_id=202005231037010100220750281C3C831D","source":"简小白","tag":"digital","tip":0,"title":"四年一遇！三星手机深夜崩溃，中招后资料难保","url":"http://open-hl.toutiao.com/a6829769856821232140/?utm_campaign=open&utm_medium=webview&utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50&item_id=6829769856821232140","user_info":{"avatar_url":"http://sf3-ttcdn-tos.pstatp.com/img/mosaic-legacy/db1700151d3228541d90~120x256.image","description":"互联网领域的知识分享","follow":false,"follower_count":0,"home_page":"http://open.toutiao.com/u95988241542/?utm_source=zx_rywnl_api&label=homepage&media_id=1595890482770951","media_id":1595890482770951,"name":"简小白","user_id":95988241542,"user_verified":true,"verified_content":"优质科技领域创作者"}},{"abstract":"笑到胃痛的穿帮镜头：刘亦菲挺起胸膛，张三丰的耐克鞋穿越了？1.本来看到这一张图片觉得没有什么奇怪的，就是特别心疼我们的神仙姐姐刘亦菲，可是再仔细看看，看到神仙姐姐挺起的胸膛，文胸的形状历历在目，可是古代是没有文胸的存在啊！","article_type":0,"article_url":"http://open-hl.toutiao.com/a6815569605851873795/?utm_campaign=open&utm_medium=webview&utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50&item_id=6815569605851873795","ban_comment":0,"behot_time":1590201232,"bury_count":0,"cell_type":0,"comment_count":81,"comment_url":"http://open.toutiao.com/a6815569605851873795/comment/?utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50","cover_image_list":[{"height":196,"uri":"img/pgc-image/14aca49cb2f54c2691d3306068139b54","url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/14aca49cb2f54c2691d3306068139b54~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/14aca49cb2f54c2691d3306068139b54~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/14aca49cb2f54c2691d3306068139b54~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/14aca49cb2f54c2691d3306068139b54~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},{"height":196,"uri":"img/pgc-image/44245881a5b44d43bd803479a49a9b9f","url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/44245881a5b44d43bd803479a49a9b9f~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/44245881a5b44d43bd803479a49a9b9f~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/44245881a5b44d43bd803479a49a9b9f~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/44245881a5b44d43bd803479a49a9b9f~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},{"height":196,"uri":"img/pgc-image/4e8f831c972d44539d603b5a137b57bf","url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/4e8f831c972d44539d603b5a137b57bf~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/4e8f831c972d44539d603b5a137b57bf~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/4e8f831c972d44539d603b5a137b57bf~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/4e8f831c972d44539d603b5a137b57bf~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300}],"cover_mode":2,"digg_count":2022,"filter_words":[{"id":"8:0","is_selected":false,"name":"看过了"},{"id":"9:1","is_selected":false,"name":"内容太水"},{"id":"5:197567889","is_selected":false,"name":"拉黑作者:考拉园艺"},{"id":"1:1629","is_selected":false,"name":"不想看:娱乐"},{"id":"6:16077","is_selected":false,"name":"不想看:刘亦菲"},{"id":"6:2506846","is_selected":false,"name":"不想看:张三丰"}],"gallary_image_count":5,"group_id":6815569605851873795,"group_source":2,"has_gallery":false,"has_video":false,"image_list":[{"height":196,"uri":"img/pgc-image/14aca49cb2f54c2691d3306068139b54","url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/14aca49cb2f54c2691d3306068139b54~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/14aca49cb2f54c2691d3306068139b54~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/14aca49cb2f54c2691d3306068139b54~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/14aca49cb2f54c2691d3306068139b54~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},{"height":196,"uri":"img/pgc-image/44245881a5b44d43bd803479a49a9b9f","url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/44245881a5b44d43bd803479a49a9b9f~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/44245881a5b44d43bd803479a49a9b9f~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/44245881a5b44d43bd803479a49a9b9f~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/44245881a5b44d43bd803479a49a9b9f~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},{"height":196,"uri":"img/pgc-image/4e8f831c972d44539d603b5a137b57bf","url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/4e8f831c972d44539d603b5a137b57bf~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/4e8f831c972d44539d603b5a137b57bf~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/4e8f831c972d44539d603b5a137b57bf~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/4e8f831c972d44539d603b5a137b57bf~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300}],"is_stick":false,"item_id":6815569605851873795,"label":"","large_image_list":[],"middle_image":{"height":196,"uri":"img/pgc-image/14aca49cb2f54c2691d3306068139b54","url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/14aca49cb2f54c2691d3306068139b54~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/14aca49cb2f54c2691d3306068139b54~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/14aca49cb2f54c2691d3306068139b54~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/14aca49cb2f54c2691d3306068139b54~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},"publish_time":1586873459,"share_count":291,"share_url":"http://open.toutiao.com/a6815569605851873795/?app=news_article&utm_source=zx_rywnl_api&utm_medium=webview&utm_campaign=open&is_hit_share_recommend=0&label=share&a_t=30159878878316673751415307019620&req_id=202005231037010100220750281C3C831D","source":"考拉园艺","tag":"news_entertainment","tip":0,"title":"笑到胃痛的穿帮镜头：刘亦菲挺起胸膛，张三丰的耐克鞋穿越了？","url":"http://open-hl.toutiao.com/a6815569605851873795/?utm_campaign=open&utm_medium=webview&utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50&item_id=6815569605851873795","user_info":{"avatar_url":"http://sf1-ttcdn-tos.pstatp.com/img/pgc-image/cf357eb8921444e4815892744e319598~120x256.image","description":"园艺花卉、绿植盆栽、花盆花瓶","follow":false,"follower_count":0,"home_page":"http://open.toutiao.com/u149140207589896/?utm_source=zx_rywnl_api&label=homepage&media_id=1658670831502349","media_id":1658670831502349,"name":"考拉园艺","user_id":149140207589896,"user_verified":false}},{"abstract":"大家都知道现在控制疫情最好的方法就是封城隔离，不过美国已经错过了这个时机，美国已经有160多万人确诊，而实际感染人数恐怕要高出好几倍，有分析人士预计是十倍以上，美国现在50州都已经重启经济，全面复工很大概率导致情况再次恶化，但是不复工也不行，美国已经有3000多万人失业，这个数字","article_type":0,"article_url":"http://open-hl.toutiao.com/a6829520966129811975/?utm_campaign=open&utm_medium=webview&utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50&item_id=6829520966129811975","ban_comment":0,"behot_time":1590201137,"bury_count":0,"cell_type":0,"comment_count":89,"comment_url":"http://open.toutiao.com/a6829520966129811975/comment/?utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50","cover_image_list":[{"height":540,"uri":"img/pgc-image/88b064adc7394dc98e9b8342a92d8f2c","url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/88b064adc7394dc98e9b8342a92d8f2c~tplv-tt-cs0:960:540.webp?from=feed","url_list":[{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/88b064adc7394dc98e9b8342a92d8f2c~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/88b064adc7394dc98e9b8342a92d8f2c~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/88b064adc7394dc98e9b8342a92d8f2c~tplv-tt-cs0:960:540.webp?from=feed"}],"width":960}],"cover_mode":1,"digg_count":14,"filter_words":[{"id":"8:0","is_selected":false,"name":"看过了"},{"id":"9:1","is_selected":false,"name":"内容太水"},{"id":"5:2164229192","is_selected":false,"name":"拉黑作者:一样的观点"},{"id":"2:11781145","is_selected":false,"name":"不想看:国际社会"},{"id":"6:15875","is_selected":false,"name":"不想看:印度"},{"id":"6:203982","is_selected":false,"name":"不想看:传染病"}],"gallary_image_count":11,"group_id":6829520966129811975,"group_source":2,"has_gallery":false,"has_video":false,"image_list":[],"is_stick":false,"item_id":6829520966129811975,"label":"","large_image_list":[{"height":540,"uri":"img/pgc-image/88b064adc7394dc98e9b8342a92d8f2c","url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/88b064adc7394dc98e9b8342a92d8f2c~tplv-tt-cs0:960:540.webp?from=feed","url_list":[{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/88b064adc7394dc98e9b8342a92d8f2c~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/88b064adc7394dc98e9b8342a92d8f2c~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/88b064adc7394dc98e9b8342a92d8f2c~tplv-tt-cs0:960:540.webp?from=feed"}],"width":960}],"middle_image":{"height":196,"uri":"img/pgc-image/88b064adc7394dc98e9b8342a92d8f2c","url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/88b064adc7394dc98e9b8342a92d8f2c~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/88b064adc7394dc98e9b8342a92d8f2c~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/88b064adc7394dc98e9b8342a92d8f2c~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/88b064adc7394dc98e9b8342a92d8f2c~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},"publish_time":1590121762,"share_count":9,"share_url":"http://open.toutiao.com/a6829520966129811975/?app=news_article&utm_source=zx_rywnl_api&utm_medium=webview&utm_campaign=open&is_hit_share_recommend=0&label=share&a_t=30159878878316673751415307019620&req_id=202005231037010100220750281C3C831D","source":"一样的观点","tag":"news_world","tip":0,"title":"5月22日海外疫情：美国疫情几乎无解，印度持续恶化，巴西已失控","url":"http://open-hl.toutiao.com/a6829520966129811975/?utm_campaign=open&utm_medium=webview&utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50&item_id=6829520966129811975","user_info":{"avatar_url":"http://sf6-ttcdn-tos.pstatp.com/img/mosaic-legacy/4724000396412a0dd13a~120x256.image","description":"每天看看新鲜事，感谢大家的关注","follow":false,"follower_count":0,"home_page":"http://open.toutiao.com/u59534859568/?utm_source=zx_rywnl_api&label=homepage&media_id=1565893230373890","media_id":1565893230373890,"name":"一样的观点","user_id":59534859568,"user_verified":false}},{"abstract":"这起命案的侦破，是来自一个母亲，对五岁失踪女儿的忧心与愧疚，到底是怎么回事呢?李苓儒信上写着自己是个失职的母亲，因为毒品及伪钞罪入狱，但自从开始服刑后，只有在2006年8月7日见过她四岁的女儿，随后，直至写信为止她已经有一年多没有女儿的消息了，加上照料女儿的男友吴文宏又完全不知去","article_type":0,"article_url":"http://open-hl.toutiao.com/a6829628626854478349/?utm_campaign=open&utm_medium=webview&utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50&item_id=6829628626854478349","ban_comment":0,"behot_time":1590201043,"bury_count":0,"cell_type":0,"comment_count":0,"comment_url":"http://open.toutiao.com/a6829628626854478349/comment/?utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50","cover_image_list":[{"height":196,"uri":"img/pgc-image/b541b18ea05545e6a140af85d02baeb1","url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/b541b18ea05545e6a140af85d02baeb1~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/b541b18ea05545e6a140af85d02baeb1~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/b541b18ea05545e6a140af85d02baeb1~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/b541b18ea05545e6a140af85d02baeb1~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},{"height":196,"uri":"img/pgc-image/800170197c4b418e996afa755fa6a56a","url":"http://p26-tt.byteimg.com/img/pgc-image/800170197c4b418e996afa755fa6a56a~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p26-tt.byteimg.com/img/pgc-image/800170197c4b418e996afa755fa6a56a~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p26-tt.byteimg.com/img/pgc-image/800170197c4b418e996afa755fa6a56a~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p26-tt.byteimg.com/img/pgc-image/800170197c4b418e996afa755fa6a56a~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},{"height":196,"uri":"img/pgc-image/36b2bb1c0e9f408381f9e3d34d103d99","url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/36b2bb1c0e9f408381f9e3d34d103d99~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/36b2bb1c0e9f408381f9e3d34d103d99~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/36b2bb1c0e9f408381f9e3d34d103d99~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/36b2bb1c0e9f408381f9e3d34d103d99~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300}],"cover_mode":2,"digg_count":2,"filter_words":[{"id":"8:0","is_selected":false,"name":"看过了"},{"id":"9:1","is_selected":false,"name":"内容太水"},{"id":"5:1317266296","is_selected":false,"name":"拉黑作者:景德镇南河公安"},{"id":"2:215598512","is_selected":false,"name":"不想看:社会负面"},{"id":"6:39955","is_selected":false,"name":"不想看:水泥"}],"gallary_image_count":4,"group_id":6829628626854478349,"group_source":2,"has_gallery":false,"has_video":false,"image_list":[{"height":196,"uri":"img/pgc-image/b541b18ea05545e6a140af85d02baeb1","url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/b541b18ea05545e6a140af85d02baeb1~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/b541b18ea05545e6a140af85d02baeb1~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/b541b18ea05545e6a140af85d02baeb1~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/b541b18ea05545e6a140af85d02baeb1~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},{"height":196,"uri":"img/pgc-image/800170197c4b418e996afa755fa6a56a","url":"http://p26-tt.byteimg.com/img/pgc-image/800170197c4b418e996afa755fa6a56a~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p26-tt.byteimg.com/img/pgc-image/800170197c4b418e996afa755fa6a56a~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p26-tt.byteimg.com/img/pgc-image/800170197c4b418e996afa755fa6a56a~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p26-tt.byteimg.com/img/pgc-image/800170197c4b418e996afa755fa6a56a~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},{"height":196,"uri":"img/pgc-image/36b2bb1c0e9f408381f9e3d34d103d99","url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/36b2bb1c0e9f408381f9e3d34d103d99~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/36b2bb1c0e9f408381f9e3d34d103d99~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/36b2bb1c0e9f408381f9e3d34d103d99~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/36b2bb1c0e9f408381f9e3d34d103d99~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300}],"is_stick":false,"item_id":6829628626854478349,"label":"","large_image_list":[],"middle_image":{"height":196,"uri":"img/pgc-image/b541b18ea05545e6a140af85d02baeb1","url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/b541b18ea05545e6a140af85d02baeb1~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/b541b18ea05545e6a140af85d02baeb1~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/b541b18ea05545e6a140af85d02baeb1~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/b541b18ea05545e6a140af85d02baeb1~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},"publish_time":1590164100,"share_count":0,"share_url":"http://open.toutiao.com/a6829628626854478349/?app=news_article&utm_source=zx_rywnl_api&utm_medium=webview&utm_campaign=open&is_hit_share_recommend=0&label=share&a_t=30159878878316673751415307019620&req_id=202005231037010100220750281C3C831D","source":"景德镇南河公安","tag":"news_society","tip":0,"title":"中国大案纪实｜水泥封尸案","url":"http://open-hl.toutiao.com/a6829628626854478349/?utm_campaign=open&utm_medium=webview&utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50&item_id=6829628626854478349","user_info":{"avatar_url":"http://sf3-ttcdn-tos.pstatp.com/img/mosaic-legacy/1a6a000d7e9023203ca3~120x256.image","description":"网上网下汇集民意，全心全意为群众做好服务","follow":false,"follower_count":0,"home_page":"http://open.toutiao.com/u58554691796/?utm_source=zx_rywnl_api&label=homepage&media_id=1563892280743937","media_id":1563892280743937,"name":"景德镇南河公安","user_id":58554691796,"user_verified":true,"verified_content":"江西省景德镇市公安局南河分局官方账号"}},{"abstract":"5月17日，是四川江油一个七岁男孩最黑暗的一天。对于突然闯入的两个人类，水牛般大小的黑熊妈妈，也许是出于护崽本能，从路边树丛中窜出，扑向母子俩。","article_type":0,"article_url":"http://open-hl.toutiao.com/a6829663346610930183/?utm_campaign=open&utm_medium=webview&utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50&item_id=6829663346610930183","ban_comment":0,"behot_time":1590200948,"bury_count":0,"cell_type":0,"comment_count":52,"comment_url":"http://open.toutiao.com/a6829663346610930183/comment/?utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50","cover_image_list":[{"height":540,"uri":"img/pgc-image/74cf559ccd974f1e97c50487213b4085","url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/74cf559ccd974f1e97c50487213b4085~tplv-tt-cs0:960:540.webp?from=feed","url_list":[{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/74cf559ccd974f1e97c50487213b4085~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/74cf559ccd974f1e97c50487213b4085~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/74cf559ccd974f1e97c50487213b4085~tplv-tt-cs0:960:540.webp?from=feed"}],"width":960}],"cover_mode":1,"digg_count":6,"filter_words":[{"id":"8:0","is_selected":false,"name":"看过了"},{"id":"9:1","is_selected":false,"name":"内容太水"},{"id":"5:2558941638","is_selected":false,"name":"拉黑作者:边城码头青年"},{"id":"1:1650","is_selected":false,"name":"不想看:情感"},{"id":"6:256020742","is_selected":false,"name":"不想看:不完美妈妈"}],"gallary_image_count":6,"group_id":6829663346610930183,"group_source":2,"has_gallery":false,"has_video":false,"image_list":[],"is_stick":false,"item_id":6829663346610930183,"label":"","large_image_list":[{"height":540,"uri":"img/pgc-image/74cf559ccd974f1e97c50487213b4085","url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/74cf559ccd974f1e97c50487213b4085~tplv-tt-cs0:960:540.webp?from=feed","url_list":[{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/74cf559ccd974f1e97c50487213b4085~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/74cf559ccd974f1e97c50487213b4085~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/74cf559ccd974f1e97c50487213b4085~tplv-tt-cs0:960:540.webp?from=feed"}],"width":960}],"middle_image":{"height":196,"uri":"img/pgc-image/74cf559ccd974f1e97c50487213b4085","url":"http://p3-tt-ipv6.byteimg.com/img/pgc-image/74cf559ccd974f1e97c50487213b4085~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p3-tt-ipv6.byteimg.com/img/pgc-image/74cf559ccd974f1e97c50487213b4085~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p3-tt-ipv6.byteimg.com/img/pgc-image/74cf559ccd974f1e97c50487213b4085~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p3-tt-ipv6.byteimg.com/img/pgc-image/74cf559ccd974f1e97c50487213b4085~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},"publish_time":1590154913,"share_count":0,"share_url":"http://open.toutiao.com/a6829663346610930183/?app=news_article&utm_source=zx_rywnl_api&utm_medium=webview&utm_campaign=open&is_hit_share_recommend=0&label=share&a_t=30159878878316673751415307019620&req_id=202005231037010100220750281C3C831D","source":"边城码头青年","tag":"emotion","tip":0,"title":"两个母亲的致命相遇：别吃我儿子，吃我","url":"http://open-hl.toutiao.com/a6829663346610930183/?utm_campaign=open&utm_medium=webview&utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50&item_id=6829663346610930183","user_info":{"avatar_url":"http://sf6-ttcdn-tos.pstatp.com/img/user-avatar/e9f5e57fd2c6cf6d6cf937940ee5693b~120x256.image","description":"媒体人，曾供职于南方都市报、新快报","follow":false,"follower_count":0,"home_page":"http://open.toutiao.com/u2862723253933035/?utm_source=zx_rywnl_api&label=homepage&media_id=1637942694538251","media_id":1637942694538251,"name":"边城码头青年","user_id":2862723253933035,"user_verified":true,"verified_content":"优质国际资讯领域创作者"}},{"abstract":"近日，舒淇的一组10年前旧照曝出。彼时36岁的她刚在第13届华表奖颁奖典礼上凭借《非诚勿扰》的出色表演获奖。典礼结束后，她受邀参加了华谊兄弟举办的庆功酒会。里面大咖云集，十分热闹。期间，舒淇和搭档葛优以及导演冯小刚等人举杯饮酒，喝醉之后她静坐在椅子上，脸上的红晕看起来格外妩媚。","article_type":0,"article_url":"http://open-hl.toutiao.com/a6829289202211881483/?utm_campaign=open&utm_medium=webview&utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50&item_id=6829289202211881483","ban_comment":0,"behot_time":1590200854,"bury_count":0,"cell_type":0,"comment_count":4,"comment_url":"http://open.toutiao.com/a6829289202211881483/comment/?utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50","cover_image_list":[{"height":196,"uri":"img/dfic-imagehandler/bbe5c30c-b61c-4502-918c-6822a3f703eb","url":"http://p6-tt-ipv6.byteimg.com/img/dfic-imagehandler/bbe5c30c-b61c-4502-918c-6822a3f703eb~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p6-tt-ipv6.byteimg.com/img/dfic-imagehandler/bbe5c30c-b61c-4502-918c-6822a3f703eb~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/dfic-imagehandler/bbe5c30c-b61c-4502-918c-6822a3f703eb~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/dfic-imagehandler/bbe5c30c-b61c-4502-918c-6822a3f703eb~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},{"height":196,"uri":"img/pgc-image/f7320b12eb4d46b7a6e173aa852a2262","url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/f7320b12eb4d46b7a6e173aa852a2262~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/f7320b12eb4d46b7a6e173aa852a2262~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/f7320b12eb4d46b7a6e173aa852a2262~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/f7320b12eb4d46b7a6e173aa852a2262~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},{"height":196,"uri":"img/pgc-image/394d5fa6fb214b038638a8fdb086e4f1","url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/394d5fa6fb214b038638a8fdb086e4f1~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/394d5fa6fb214b038638a8fdb086e4f1~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/394d5fa6fb214b038638a8fdb086e4f1~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/394d5fa6fb214b038638a8fdb086e4f1~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300}],"cover_mode":2,"digg_count":135,"filter_words":[{"id":"8:0","is_selected":false,"name":"看过了"},{"id":"9:1","is_selected":false,"name":"内容太水"},{"id":"5:2802237300","is_selected":false,"name":"拉黑作者:娱评之窗"},{"id":"1:1629","is_selected":false,"name":"不想看:娱乐"},{"id":"6:17816","is_selected":false,"name":"不想看:冯小刚"},{"id":"6:17489","is_selected":false,"name":"不想看:舒淇"}],"gallary_image_count":9,"group_id":6829289202211881483,"group_source":2,"has_gallery":false,"has_video":false,"image_list":[{"height":196,"uri":"img/dfic-imagehandler/bbe5c30c-b61c-4502-918c-6822a3f703eb","url":"http://p6-tt-ipv6.byteimg.com/img/dfic-imagehandler/bbe5c30c-b61c-4502-918c-6822a3f703eb~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p6-tt-ipv6.byteimg.com/img/dfic-imagehandler/bbe5c30c-b61c-4502-918c-6822a3f703eb~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/dfic-imagehandler/bbe5c30c-b61c-4502-918c-6822a3f703eb~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/dfic-imagehandler/bbe5c30c-b61c-4502-918c-6822a3f703eb~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},{"height":196,"uri":"img/pgc-image/f7320b12eb4d46b7a6e173aa852a2262","url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/f7320b12eb4d46b7a6e173aa852a2262~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/f7320b12eb4d46b7a6e173aa852a2262~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/f7320b12eb4d46b7a6e173aa852a2262~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/f7320b12eb4d46b7a6e173aa852a2262~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},{"height":196,"uri":"img/pgc-image/394d5fa6fb214b038638a8fdb086e4f1","url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/394d5fa6fb214b038638a8fdb086e4f1~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/394d5fa6fb214b038638a8fdb086e4f1~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/394d5fa6fb214b038638a8fdb086e4f1~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/394d5fa6fb214b038638a8fdb086e4f1~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300}],"is_stick":false,"item_id":6829289202211881483,"label":"","large_image_list":[],"middle_image":{"height":196,"uri":"img/dfic-imagehandler/bbe5c30c-b61c-4502-918c-6822a3f703eb","url":"http://p3-tt-ipv6.byteimg.com/img/dfic-imagehandler/bbe5c30c-b61c-4502-918c-6822a3f703eb~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p3-tt-ipv6.byteimg.com/img/dfic-imagehandler/bbe5c30c-b61c-4502-918c-6822a3f703eb~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p3-tt-ipv6.byteimg.com/img/dfic-imagehandler/bbe5c30c-b61c-4502-918c-6822a3f703eb~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p3-tt-ipv6.byteimg.com/img/dfic-imagehandler/bbe5c30c-b61c-4502-918c-6822a3f703eb~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},"publish_time":1590067800,"share_count":9,"share_url":"http://open.toutiao.com/a6829289202211881483/?app=news_article&utm_source=zx_rywnl_api&utm_medium=webview&utm_campaign=open&is_hit_share_recommend=0&label=share&a_t=30159878878316673751415307019620&req_id=202005231037010100220750281C3C831D","source":"娱评之窗","tag":"news_entertainment","tip":0,"title":"舒淇10年前：和冯小刚亲密牵手，醉酒后风情万种","url":"http://open-hl.toutiao.com/a6829289202211881483/?utm_campaign=open&utm_medium=webview&utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50&item_id=6829289202211881483","user_info":{"avatar_url":"http://sf3-ttcdn-tos.pstatp.com/img/pgc-image/30bee5b4d3354780b9fb8b22d9818c68~120x256.image","description":"深度娱评，多重视角，多彩人生！","follow":false,"follower_count":0,"home_page":"http://open.toutiao.com/u4037029607259789/?utm_source=zx_rywnl_api&label=homepage&media_id=1666825696305163","media_id":1666825696305163,"name":"娱评之窗","user_id":4037029607259789,"user_verified":false}},{"abstract":"这两天美国舆论的一个焦点\u2014\u2014特朗普究竟有没有戴口罩，终于有了答案。该州规定，任何身处密闭空间内的民众都要佩戴口罩，但特朗普在会见媒体时并没有戴口罩，而周围的人都戴了。","article_type":0,"article_url":"http://open-hl.toutiao.com/a6829592451812950541/?utm_campaign=open&utm_medium=webview&utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50&item_id=6829592451812950541","ban_comment":1,"behot_time":1590200759,"bury_count":0,"cell_type":0,"comment_count":0,"comment_url":"http://open.toutiao.com/a6829592451812950541/comment/?utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50","cover_image_list":[{"height":540,"uri":"img/pgc-image/RzhqhBi5CSNMJw","url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/RzhqhBi5CSNMJw~tplv-tt-cs0:960:540.webp?from=feed","url_list":[{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/RzhqhBi5CSNMJw~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/RzhqhBi5CSNMJw~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/RzhqhBi5CSNMJw~tplv-tt-cs0:960:540.webp?from=feed"}],"width":960}],"cover_mode":1,"digg_count":585,"filter_words":[{"id":"8:0","is_selected":false,"name":"看过了"},{"id":"9:1","is_selected":false,"name":"内容太水"},{"id":"5:1574652238","is_selected":false,"name":"拉黑作者:二三里资讯"},{"id":"1:1645","is_selected":false,"name":"不想看:国际"},{"id":"6:15642","is_selected":false,"name":"不想看:口罩"}],"gallary_image_count":1,"group_id":6829592451812950541,"group_source":2,"has_gallery":false,"has_video":false,"image_list":[],"is_stick":false,"item_id":6829592451812950541,"label":"","large_image_list":[{"height":540,"uri":"img/pgc-image/RzhqhBi5CSNMJw","url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/RzhqhBi5CSNMJw~tplv-tt-cs0:960:540.webp?from=feed","url_list":[{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/RzhqhBi5CSNMJw~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/RzhqhBi5CSNMJw~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/RzhqhBi5CSNMJw~tplv-tt-cs0:960:540.webp?from=feed"}],"width":960}],"middle_image":{"height":196,"uri":"img/pgc-image/RzhqhBi5CSNMJw","url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/RzhqhBi5CSNMJw~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/RzhqhBi5CSNMJw~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/RzhqhBi5CSNMJw~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/RzhqhBi5CSNMJw~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},"publish_time":1590138406,"share_count":131,"share_url":"http://open.toutiao.com/a6829592451812950541/?app=news_article&utm_source=zx_rywnl_api&utm_medium=webview&utm_campaign=open&is_hit_share_recommend=0&label=share&a_t=30159878878316673751415307019620&req_id=202005231037010100220750281C3C831D","source":"二三里资讯","tag":"news_world","tip":0,"title":"他终于戴口罩了，定制款","url":"http://open-hl.toutiao.com/a6829592451812950541/?utm_campaign=open&utm_medium=webview&utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50&item_id=6829592451812950541","user_info":{"avatar_url":"http://sf3-ttcdn-tos.pstatp.com/img/user-avatar/9ca7d5d90dd832d8f1bfad07c0a2aab5~120x256.image","description":"二三里资讯\u2014\u2014离你更近的生活","follow":false,"follower_count":0,"home_page":"http://open.toutiao.com/u95766457919/?utm_source=zx_rywnl_api&label=homepage&media_id=1595615971138567","media_id":1595615971138567,"name":"二三里资讯","user_id":95766457919,"user_verified":true,"verified_content":"二三里资讯官方账号"}},{"abstract":"国家近几年来大力提倡新能源政策，大力支持和补贴新能源车企，于是许多车企也就抓住了这个机会，纷纷加入到了新能源的阵营里来，像比亚迪之前一直在国内的汽车市场上不温不火，但是通过新能源这个机会也是在国内市场站稳了脚跟，还有野心要成为\u201c新能源第一\u201d的壮志。","article_type":0,"article_url":"http://open-hl.toutiao.com/a6828122709881258499/?utm_campaign=open&utm_medium=webview&utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50&item_id=6828122709881258499","ban_comment":0,"behot_time":1590200665,"bury_count":0,"cell_type":0,"comment_count":102,"comment_url":"http://open.toutiao.com/a6828122709881258499/comment/?utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50","cover_image_list":[{"height":196,"uri":"img/pgc-image/ceb4cb87bcaf4153a99c622bed855f11","url":"http://p9-tt-ipv6.byteimg.com/img/pgc-image/ceb4cb87bcaf4153a99c622bed855f11~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p9-tt-ipv6.byteimg.com/img/pgc-image/ceb4cb87bcaf4153a99c622bed855f11~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p9-tt-ipv6.byteimg.com/img/pgc-image/ceb4cb87bcaf4153a99c622bed855f11~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p9-tt-ipv6.byteimg.com/img/pgc-image/ceb4cb87bcaf4153a99c622bed855f11~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},{"height":196,"uri":"img/pgc-image/bee8a47adf7c4adbb8e93c7b842cf724","url":"http://p9-tt-ipv6.byteimg.com/img/pgc-image/bee8a47adf7c4adbb8e93c7b842cf724~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p9-tt-ipv6.byteimg.com/img/pgc-image/bee8a47adf7c4adbb8e93c7b842cf724~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p9-tt-ipv6.byteimg.com/img/pgc-image/bee8a47adf7c4adbb8e93c7b842cf724~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p9-tt-ipv6.byteimg.com/img/pgc-image/bee8a47adf7c4adbb8e93c7b842cf724~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},{"height":196,"uri":"img/pgc-image/dbc858eeee5c4e3cbecc2926684465a6","url":"http://p26-tt.byteimg.com/img/pgc-image/dbc858eeee5c4e3cbecc2926684465a6~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p26-tt.byteimg.com/img/pgc-image/dbc858eeee5c4e3cbecc2926684465a6~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p26-tt.byteimg.com/img/pgc-image/dbc858eeee5c4e3cbecc2926684465a6~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p26-tt.byteimg.com/img/pgc-image/dbc858eeee5c4e3cbecc2926684465a6~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300}],"cover_mode":2,"digg_count":253,"filter_words":[{"id":"8:0","is_selected":false,"name":"看过了"},{"id":"9:1","is_selected":false,"name":"内容太水"},{"id":"5:2787267440","is_selected":false,"name":"拉黑作者:小锤聊汽车"},{"id":"2:31137486","is_selected":false,"name":"不想看:汽车产业"},{"id":"6:47606","is_selected":false,"name":"不想看:新能源汽车"}],"gallary_image_count":6,"group_id":6828122709881258499,"group_source":2,"has_gallery":false,"has_video":false,"image_list":[{"height":196,"uri":"img/pgc-image/ceb4cb87bcaf4153a99c622bed855f11","url":"http://p9-tt-ipv6.byteimg.com/img/pgc-image/ceb4cb87bcaf4153a99c622bed855f11~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p9-tt-ipv6.byteimg.com/img/pgc-image/ceb4cb87bcaf4153a99c622bed855f11~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p9-tt-ipv6.byteimg.com/img/pgc-image/ceb4cb87bcaf4153a99c622bed855f11~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p9-tt-ipv6.byteimg.com/img/pgc-image/ceb4cb87bcaf4153a99c622bed855f11~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},{"height":196,"uri":"img/pgc-image/bee8a47adf7c4adbb8e93c7b842cf724","url":"http://p9-tt-ipv6.byteimg.com/img/pgc-image/bee8a47adf7c4adbb8e93c7b842cf724~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p9-tt-ipv6.byteimg.com/img/pgc-image/bee8a47adf7c4adbb8e93c7b842cf724~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p9-tt-ipv6.byteimg.com/img/pgc-image/bee8a47adf7c4adbb8e93c7b842cf724~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p9-tt-ipv6.byteimg.com/img/pgc-image/bee8a47adf7c4adbb8e93c7b842cf724~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},{"height":196,"uri":"img/pgc-image/dbc858eeee5c4e3cbecc2926684465a6","url":"http://p26-tt.byteimg.com/img/pgc-image/dbc858eeee5c4e3cbecc2926684465a6~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p26-tt.byteimg.com/img/pgc-image/dbc858eeee5c4e3cbecc2926684465a6~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p26-tt.byteimg.com/img/pgc-image/dbc858eeee5c4e3cbecc2926684465a6~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p26-tt.byteimg.com/img/pgc-image/dbc858eeee5c4e3cbecc2926684465a6~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300}],"is_stick":false,"item_id":6828122709881258499,"label":"","large_image_list":[],"middle_image":{"height":196,"uri":"img/pgc-image/ceb4cb87bcaf4153a99c622bed855f11","url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/ceb4cb87bcaf4153a99c622bed855f11~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/ceb4cb87bcaf4153a99c622bed855f11~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/ceb4cb87bcaf4153a99c622bed855f11~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/ceb4cb87bcaf4153a99c622bed855f11~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},"publish_time":1589869327,"share_count":71,"share_url":"http://open.toutiao.com/a6828122709881258499/?app=news_article&utm_source=zx_rywnl_api&utm_medium=webview&utm_campaign=open&is_hit_share_recommend=0&label=share&a_t=30159878878316673751415307019620&req_id=202005231037010100220750281C3C831D","source":"小锤聊汽车","tag":"news_tech","tip":0,"title":"东北一场大暴雨，彻底揭开新能源的遮羞布，车主：谁买谁后悔","url":"http://open-hl.toutiao.com/a6828122709881258499/?utm_campaign=open&utm_medium=webview&utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50&item_id=6828122709881258499","user_info":{"avatar_url":"http://sf3-ttcdn-tos.pstatp.com/img/pgc-image/cf7eb005f4984eb5bcbfd77237153697~120x256.image","description":"每天给你们带来汽车的趣事","follow":false,"follower_count":0,"home_page":"http://open.toutiao.com/u2334962563955491/?utm_source=zx_rywnl_api&label=homepage&media_id=1662753788693516","media_id":1662753788693516,"name":"小锤聊汽车","user_id":2334962563955491,"user_verified":true,"verified_content":"优质历史领域创作者"}},{"abstract":"中国检察网5月15日公布一起《关于撤销张某某监护人资格支持起诉案》的案例，案情显示张某某与钟某某结婚后，于2008年至2015年期间先后生育三个女孩张甲、张乙和张丙。","article_type":0,"article_url":"http://open-hl.toutiao.com/a6828862372627612163/?utm_campaign=open&utm_medium=webview&utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50&item_id=6828862372627612163","ban_comment":0,"behot_time":1590200570,"bury_count":2,"cell_type":0,"comment_count":54,"comment_url":"http://open.toutiao.com/a6828862372627612163/comment/?utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50","cover_image_list":[{"height":540,"uri":"img/dfic-imagehandler/28f3a33f-b5f8-4784-9328-cd0b519ca174","url":"http://p26-tt.byteimg.com/img/dfic-imagehandler/28f3a33f-b5f8-4784-9328-cd0b519ca174~tplv-tt-cs0:960:540.webp?from=feed","url_list":[{"url":"http://p26-tt.byteimg.com/img/dfic-imagehandler/28f3a33f-b5f8-4784-9328-cd0b519ca174~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p26-tt.byteimg.com/img/dfic-imagehandler/28f3a33f-b5f8-4784-9328-cd0b519ca174~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p26-tt.byteimg.com/img/dfic-imagehandler/28f3a33f-b5f8-4784-9328-cd0b519ca174~tplv-tt-cs0:960:540.webp?from=feed"}],"width":960}],"cover_mode":1,"digg_count":1759,"filter_words":[{"id":"8:0","is_selected":false,"name":"看过了"},{"id":"9:1","is_selected":false,"name":"内容太水"},{"id":"5:2565233848","is_selected":false,"name":"拉黑作者:山西融媒体快报"},{"id":"3:306442034","is_selected":false,"name":"不想看:法律"},{"id":"6:88971","is_selected":false,"name":"不想看:监护人"}],"gallary_image_count":1,"group_id":6828862372627612163,"group_source":2,"has_gallery":false,"has_video":false,"image_list":[],"is_stick":false,"item_id":6828862372627612163,"label":"","large_image_list":[{"height":540,"uri":"img/dfic-imagehandler/28f3a33f-b5f8-4784-9328-cd0b519ca174","url":"http://p26-tt.byteimg.com/img/dfic-imagehandler/28f3a33f-b5f8-4784-9328-cd0b519ca174~tplv-tt-cs0:960:540.webp?from=feed","url_list":[{"url":"http://p26-tt.byteimg.com/img/dfic-imagehandler/28f3a33f-b5f8-4784-9328-cd0b519ca174~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p26-tt.byteimg.com/img/dfic-imagehandler/28f3a33f-b5f8-4784-9328-cd0b519ca174~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p26-tt.byteimg.com/img/dfic-imagehandler/28f3a33f-b5f8-4784-9328-cd0b519ca174~tplv-tt-cs0:960:540.webp?from=feed"}],"width":960}],"middle_image":{"height":196,"uri":"img/dfic-imagehandler/28f3a33f-b5f8-4784-9328-cd0b519ca174","url":"http://p9-tt-ipv6.byteimg.com/img/dfic-imagehandler/28f3a33f-b5f8-4784-9328-cd0b519ca174~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p9-tt-ipv6.byteimg.com/img/dfic-imagehandler/28f3a33f-b5f8-4784-9328-cd0b519ca174~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p9-tt-ipv6.byteimg.com/img/dfic-imagehandler/28f3a33f-b5f8-4784-9328-cd0b519ca174~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p9-tt-ipv6.byteimg.com/img/dfic-imagehandler/28f3a33f-b5f8-4784-9328-cd0b519ca174~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},"publish_time":1589968421,"share_count":319,"share_url":"http://open.toutiao.com/a6828862372627612163/?app=news_article&utm_source=zx_rywnl_api&utm_medium=webview&utm_campaign=open&is_hit_share_recommend=0&label=share&a_t=30159878878316673751415307019620&req_id=202005231037010100220750281C3C831D","source":"山西融媒体快报","tag":"news_society","tip":0,"title":"毫无人性，妻子病逝后男子多次强奸女儿，最终被法院撤销监护人资格","url":"http://open-hl.toutiao.com/a6828862372627612163/?utm_campaign=open&utm_medium=webview&utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50&item_id=6828862372627612163","user_info":{"avatar_url":"http://sf6-ttcdn-tos.pstatp.com/img/pgc-image/7c4f1dfc0ca1413a91e8301fe117ca26~120x256.image","description":"《山西广播电视报》社融媒体中心官方账号","follow":false,"follower_count":0,"home_page":"http://open.toutiao.com/u109014642499/?utm_source=zx_rywnl_api&label=homepage&media_id=1639841891467268","media_id":1639841891467268,"name":"山西融媒体快报","user_id":109014642499,"user_verified":true,"verified_content":"山西融媒体快报官方账号"}},{"abstract":"在德云社创立初期，那个时候的相声比较小众，不像如今一样，一说到相声大家都知道是什么。而且提到相声，大部分人第一个想到应该是\u201c郭德纲\u201d。","article_type":0,"article_url":"http://open-hl.toutiao.com/a6828533062205178379/?utm_campaign=open&utm_medium=webview&utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50&item_id=6828533062205178379","ban_comment":0,"behot_time":1590200476,"bury_count":0,"cell_type":0,"comment_count":5822,"comment_url":"http://open.toutiao.com/a6828533062205178379/comment/?utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50","cover_image_list":[{"height":196,"uri":"img/pgc-image/356f36310fbc4312b182ea1c2cbda1fa","url":"http://p26-tt.byteimg.com/img/pgc-image/356f36310fbc4312b182ea1c2cbda1fa~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p26-tt.byteimg.com/img/pgc-image/356f36310fbc4312b182ea1c2cbda1fa~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p26-tt.byteimg.com/img/pgc-image/356f36310fbc4312b182ea1c2cbda1fa~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p26-tt.byteimg.com/img/pgc-image/356f36310fbc4312b182ea1c2cbda1fa~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},{"height":196,"uri":"img/pgc-image/be483dd77c644a54817b5094e8026868","url":"http://p26-tt.byteimg.com/img/pgc-image/be483dd77c644a54817b5094e8026868~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p26-tt.byteimg.com/img/pgc-image/be483dd77c644a54817b5094e8026868~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p26-tt.byteimg.com/img/pgc-image/be483dd77c644a54817b5094e8026868~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p26-tt.byteimg.com/img/pgc-image/be483dd77c644a54817b5094e8026868~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},{"height":196,"uri":"img/pgc-image/b84cf42688134480846179e800ef3da5","url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/b84cf42688134480846179e800ef3da5~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/b84cf42688134480846179e800ef3da5~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/b84cf42688134480846179e800ef3da5~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/b84cf42688134480846179e800ef3da5~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300}],"cover_mode":2,"digg_count":45769,"filter_words":[{"id":"8:0","is_selected":false,"name":"看过了"},{"id":"9:1","is_selected":false,"name":"内容太水"},{"id":"5:2732165066","is_selected":false,"name":"拉黑作者:kiss娱先生"},{"id":"2:306463983","is_selected":false,"name":"不想看:相声"},{"id":"6:854515","is_selected":false,"name":"不想看:刘云天"},{"id":"6:16293","is_selected":false,"name":"不想看:郭德纲"}],"gallary_image_count":23,"group_id":6828533062205178379,"group_source":2,"has_gallery":false,"has_video":false,"image_list":[{"height":196,"uri":"img/pgc-image/356f36310fbc4312b182ea1c2cbda1fa","url":"http://p26-tt.byteimg.com/img/pgc-image/356f36310fbc4312b182ea1c2cbda1fa~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p26-tt.byteimg.com/img/pgc-image/356f36310fbc4312b182ea1c2cbda1fa~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p26-tt.byteimg.com/img/pgc-image/356f36310fbc4312b182ea1c2cbda1fa~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p26-tt.byteimg.com/img/pgc-image/356f36310fbc4312b182ea1c2cbda1fa~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},{"height":196,"uri":"img/pgc-image/be483dd77c644a54817b5094e8026868","url":"http://p26-tt.byteimg.com/img/pgc-image/be483dd77c644a54817b5094e8026868~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p26-tt.byteimg.com/img/pgc-image/be483dd77c644a54817b5094e8026868~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p26-tt.byteimg.com/img/pgc-image/be483dd77c644a54817b5094e8026868~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p26-tt.byteimg.com/img/pgc-image/be483dd77c644a54817b5094e8026868~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},{"height":196,"uri":"img/pgc-image/b84cf42688134480846179e800ef3da5","url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/b84cf42688134480846179e800ef3da5~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/b84cf42688134480846179e800ef3da5~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/b84cf42688134480846179e800ef3da5~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p1-tt-ipv6.byteimg.com/img/pgc-image/b84cf42688134480846179e800ef3da5~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300}],"is_stick":false,"item_id":6828533062205178379,"label":"","large_image_list":[],"middle_image":{"height":196,"uri":"img/pgc-image/356f36310fbc4312b182ea1c2cbda1fa","url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/356f36310fbc4312b182ea1c2cbda1fa~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/356f36310fbc4312b182ea1c2cbda1fa~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/356f36310fbc4312b182ea1c2cbda1fa~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/356f36310fbc4312b182ea1c2cbda1fa~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},"publish_time":1589891748,"share_count":2798,"share_url":"http://open.toutiao.com/a6828533062205178379/?app=news_article&utm_source=zx_rywnl_api&utm_medium=webview&utm_campaign=open&is_hit_share_recommend=0&label=share&a_t=30159878878316673751415307019620&req_id=202005231037010100220750281C3C831D","source":"kiss娱先生","tag":"news_entertainment","tip":0,"title":"背叛郭德纲的\u201c德云四少\u201d，现如今是一个不如一个，数刘云天最惨","url":"http://open-hl.toutiao.com/a6828533062205178379/?utm_campaign=open&utm_medium=webview&utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50&item_id=6828533062205178379","user_info":{"avatar_url":"http://sf1-ttcdn-tos.pstatp.com/img/user-avatar/6b7d5b820f091313e5a7f30b492416d7~120x256.image","description":"聊明星谈娱乐，趣味的故事，百态的人生","follow":false,"follower_count":0,"home_page":"http://open.toutiao.com/u1011162828517135/?utm_source=zx_rywnl_api&label=homepage&media_id=1659942459211787","media_id":1659942459211787,"name":"kiss娱先生","user_id":1011162828517135,"user_verified":true,"verified_content":"优质娱乐领域创作者"}},{"abstract":"一出生便是万人瞩目的小公主朴槿惠，父亲朴正熙作为韩国总统，给朴槿惠的童年生活带来的是幸福美满。可偏偏上天却亲手剥夺走她的一切，在遭遇了父母双双被刺杀之后，朴槿惠知道自己已经无法回头了，她要替父母正名。","article_type":0,"article_url":"http://open-hl.toutiao.com/a6824382318988231181/?utm_campaign=open&utm_medium=webview&utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50&item_id=6824382318988231181","ban_comment":0,"behot_time":1590200381,"bury_count":0,"cell_type":0,"comment_count":528,"comment_url":"http://open.toutiao.com/a6824382318988231181/comment/?utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50","cover_image_list":[{"height":540,"uri":"img/pgc-image/78e0fbab3e054d8897f29fcf99675269","url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/78e0fbab3e054d8897f29fcf99675269~tplv-tt-cs0:960:540.webp?from=feed","url_list":[{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/78e0fbab3e054d8897f29fcf99675269~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/78e0fbab3e054d8897f29fcf99675269~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/78e0fbab3e054d8897f29fcf99675269~tplv-tt-cs0:960:540.webp?from=feed"}],"width":960}],"cover_mode":1,"digg_count":4362,"filter_words":[{"id":"8:0","is_selected":false,"name":"看过了"},{"id":"9:1","is_selected":false,"name":"内容太水"},{"id":"5:754015111","is_selected":false,"name":"拉黑作者:聊聊历史那些事"},{"id":"1:1645","is_selected":false,"name":"不想看:国际"},{"id":"6:23136","is_selected":false,"name":"不想看:朴槿惠"}],"gallary_image_count":3,"group_id":6824382318988231181,"group_source":2,"has_gallery":false,"has_video":false,"image_list":[],"is_stick":false,"item_id":6824382318988231181,"label":"","large_image_list":[{"height":540,"uri":"img/pgc-image/78e0fbab3e054d8897f29fcf99675269","url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/78e0fbab3e054d8897f29fcf99675269~tplv-tt-cs0:960:540.webp?from=feed","url_list":[{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/78e0fbab3e054d8897f29fcf99675269~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/78e0fbab3e054d8897f29fcf99675269~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/78e0fbab3e054d8897f29fcf99675269~tplv-tt-cs0:960:540.webp?from=feed"}],"width":960}],"middle_image":{"height":196,"uri":"img/pgc-image/78e0fbab3e054d8897f29fcf99675269","url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/78e0fbab3e054d8897f29fcf99675269~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/78e0fbab3e054d8897f29fcf99675269~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/78e0fbab3e054d8897f29fcf99675269~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/78e0fbab3e054d8897f29fcf99675269~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300},"publish_time":1588925328,"share_count":823,"share_url":"http://open.toutiao.com/a6824382318988231181/?app=news_article&utm_source=zx_rywnl_api&utm_medium=webview&utm_campaign=open&is_hit_share_recommend=0&label=share&a_t=30159878878316673751415307019620&req_id=202005231037010100220750281C3C831D","source":"聊聊历史那些事","tag":"news_world","tip":0,"title":"在监狱度余生！再见了，朴槿惠","url":"http://open-hl.toutiao.com/a6824382318988231181/?utm_campaign=open&utm_medium=webview&utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50&item_id=6824382318988231181","user_info":{"avatar_url":"http://sf6-ttcdn-tos.pstatp.com/img/pgc-image/3ef18c797c5c4b608104b944efa05c17~120x256.image","description":"讲述中国的人物故事，还原最真实的历史。","follow":false,"follower_count":0,"home_page":"http://open.toutiao.com/u2985906760852291/?utm_source=zx_rywnl_api&label=homepage&media_id=1650542910305287","media_id":1650542910305287,"name":"聊聊历史那些事","user_id":2985906760852291,"user_verified":false}}]
     * has_more : true
     * message : success
     * msg : success
     * req_id : 202005231037010100220750281C3C831D
     * ret : 0
     */

    private boolean has_more;
    private String message;
    private String msg;
    private String req_id;
    private int ret;
    private List<DataBean> data;

    public boolean isHas_more() {
        return has_more;
    }

    public void setHas_more(boolean has_more) {
        this.has_more = has_more;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getReq_id() {
        return req_id;
    }

    public void setReq_id(String req_id) {
        this.req_id = req_id;
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * abstract : 1.脱掉内衣胸没那么大2.无限循环FLAG+“真香”3.男生认为无聊的细节，女生往往很重视4.喜欢说反话5.不喜欢听大道理其实，真爱你的人是不会因为这些嫌弃你的
         * article_type : 0
         * article_url : http://open-hl.toutiao.com/a6801328491112432139/?utm_campaign=open&utm_medium=webview&utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50&item_id=6801328491112432139
         * ban_comment : 0
         * behot_time : 1590201421
         * bury_count : 0
         * cell_type : 0
         * comment_count : 315
         * comment_url : http://open.toutiao.com/a6801328491112432139/comment/?utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50
         * cover_image_list : [{"height":540,"uri":"img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586","url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:960:540.webp?from=feed","url_list":[{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:960:540.webp?from=feed"}],"width":960}]
         * cover_mode : 1
         * digg_count : 711
         * filter_words : [{"id":"8:0","is_selected":false,"name":"看过了"},{"id":"9:1","is_selected":false,"name":"内容太水"},{"id":"5:2163777269","is_selected":false,"name":"拉黑作者:就要花里胡哨"},{"id":"6:734672582","is_selected":false,"name":"不想看:交个朋友吧"}]
         * gallary_image_count : 5
         * group_id : 6801328491112432139
         * group_source : 2
         * has_gallery : false
         * has_video : false
         * image_list : []
         * is_stick : false
         * item_id : 6801328491112432139
         * label :
         * large_image_list : [{"height":540,"uri":"img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586","url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:960:540.webp?from=feed","url_list":[{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:960:540.webp?from=feed"}],"width":960}]
         * middle_image : {"height":196,"uri":"img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586","url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:300:196.webp?from=feed","url_list":[{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:300:196.webp?from=feed"}],"width":300}
         * publish_time : 1583557690
         * share_count : 192
         * share_url : http://open.toutiao.com/a6801328491112432139/?app=news_article&utm_source=zx_rywnl_api&utm_medium=webview&utm_campaign=open&is_hit_share_recommend=0&label=share&a_t=30159878878316673751415307019620&req_id=202005231037010100220750281C3C831D
         * source : 就要花里胡哨
         * tag : news
         * tip : 0
         * title : 男朋友眼中的你，哈哈哈
         * url : http://open-hl.toutiao.com/a6801328491112432139/?utm_campaign=open&utm_medium=webview&utm_source=zx_rywnl_api&req_id=202005231037010100220750281C3C831D&dt=JKM-Al00a&crypt=5208&label=click_open_common_channel&a_t=30159878878316673751415307019620&gy=fb510ddda6491ac2ddca3e76abc2531e856dff3b8764372c4a4860215faac30089db0a90c49366df01e1827a9951b5f552b2e6ce18847c03787d52d955e57a48589ff70042f76d67476f62f3012dfdd3a678b5f120f3bb5e968d3cee9bed2ca66c04f524f66a40080a664beece043e50&item_id=6801328491112432139
         * user_info : {"avatar_url":"http://sf6-ttcdn-tos.pstatp.com/img/pgc-image/719bfb75c812480d9ebf23009317ae0d~120x256.image","description":"这个人很懒，什么都没留下。","follow":false,"follower_count":0,"home_page":"http://open.toutiao.com/u63675336389/?utm_source=zx_rywnl_api&label=homepage&media_id=1659441453405198","media_id":1659441453405198,"name":"就要花里胡哨","user_id":63675336389,"user_verified":false}
         */

        @SerializedName("abstract")
        private String abstractX;
        private int article_type;
        private String article_url;
        private int ban_comment;
        private long behot_time;
        private int bury_count;
        private int cell_type;
        private int comment_count;
        private String comment_url;
        private int cover_mode;
        private int digg_count;
        private int gallary_image_count;
        private long group_id;
        private int group_source;
        private boolean has_gallery;
        private boolean has_video;
        private long video_duration;
        private String video_id;
        private boolean is_stick;
        private long item_id;
        private String label;
        private MiddleImageBean middle_image;
        private Long publish_time;
        private int share_count;
        private String share_url;
        private String source;
        private String tag;
        private int tip;
        private String title;
        private String url;
        private UserInfoBean user_info;
        private List<CoverImageListBean> cover_image_list;
        private List<FilterWordsBean> filter_words;
        private List<?> image_list;
        private List<LargeImageListBean> large_image_list;

//        private transient NewsEntity adEntity;
        private transient String adSlot;
        private transient int adLoadState;

        public String getVideo_id() {
            return video_id;
        }

        public void setVideo_id(String video_id) {
            this.video_id = video_id;
        }

        public String getAbstractX() {
            return abstractX;
        }

        public void setAbstractX(String abstractX) {
            this.abstractX = abstractX;
        }

        public int getArticle_type() {
            return article_type;
        }

        public void setArticle_type(int article_type) {
            this.article_type = article_type;
        }

        public String getArticle_url() {
            return article_url;
        }

        public void setArticle_url(String article_url) {
            this.article_url = article_url;
        }

        public int getBan_comment() {
            return ban_comment;
        }

        public void setBan_comment(int ban_comment) {
            this.ban_comment = ban_comment;
        }

        public long getBehot_time() {
            return behot_time;
        }

        public void setBehot_time(long behot_time) {
            this.behot_time = behot_time;
        }

        public int getBury_count() {
            return bury_count;
        }

        public void setBury_count(int bury_count) {
            this.bury_count = bury_count;
        }

        public int getCell_type() {
            return cell_type;
        }

        public void setCell_type(int cell_type) {
            this.cell_type = cell_type;
        }

        public int getComment_count() {
            return comment_count;
        }

        public void setComment_count(int comment_count) {
            this.comment_count = comment_count;
        }

        public String getComment_url() {
            return comment_url;
        }

        public void setComment_url(String comment_url) {
            this.comment_url = comment_url;
        }

        public int getCover_mode() {
            return cover_mode;
        }

        public void setCover_mode(int cover_mode) {
            this.cover_mode = cover_mode;
        }

        public int getDigg_count() {
            return digg_count;
        }

        public void setDigg_count(int digg_count) {
            this.digg_count = digg_count;
        }

        public int getGallary_image_count() {
            return gallary_image_count;
        }

        public void setGallary_image_count(int gallary_image_count) {
            this.gallary_image_count = gallary_image_count;
        }

        public long getGroup_id() {
            return group_id;
        }

        public void setGroup_id(long group_id) {
            this.group_id = group_id;
        }

        public int getGroup_source() {
            return group_source;
        }

        public void setGroup_source(int group_source) {
            this.group_source = group_source;
        }

        public boolean isHas_gallery() {
            return has_gallery;
        }

        public void setHas_gallery(boolean has_gallery) {
            this.has_gallery = has_gallery;
        }

        public boolean isHas_video() {
            return has_video;
        }

        public void setHas_video(boolean has_video) {
            this.has_video = has_video;
        }
        public long video_duration() {
            return video_duration;
        }

        public void setVideo_duration(long video_duration) {
            this.video_duration = video_duration;
        }
        public boolean isIs_stick() {
            return is_stick;
        }

        public void setIs_stick(boolean is_stick) {
            this.is_stick = is_stick;
        }

        public long getItem_id() {
            return item_id;
        }

        public void setItem_id(long item_id) {
            this.item_id = item_id;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public MiddleImageBean getMiddle_image() {
            return middle_image;
        }

        public void setMiddle_image(MiddleImageBean middle_image) {
            this.middle_image = middle_image;
        }

        public Long getPublish_time() {
            return publish_time;
        }

        public void setPublish_time(Long publish_time) {
            this.publish_time = publish_time;
        }

        public int getShare_count() {
            return share_count;
        }

        public void setShare_count(int share_count) {
            this.share_count = share_count;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public int getTip() {
            return tip;
        }

        public void setTip(int tip) {
            this.tip = tip;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public UserInfoBean getUser_info() {
            return user_info;
        }

        public void setUser_info(UserInfoBean user_info) {
            this.user_info = user_info;
        }

        public List<CoverImageListBean> getCover_image_list() {
            return cover_image_list;
        }

        public void setCover_image_list(List<CoverImageListBean> cover_image_list) {
            this.cover_image_list = cover_image_list;
        }

        public List<FilterWordsBean> getFilter_words() {
            return filter_words;
        }

        public void setFilter_words(List<FilterWordsBean> filter_words) {
            this.filter_words = filter_words;
        }

        public List<?> getImage_list() {
            return image_list;
        }

        public void setImage_list(List<?> image_list) {
            this.image_list = image_list;
        }

        public List<LargeImageListBean> getLarge_image_list() {
            return large_image_list;
        }

        public void setLarge_image_list(List<LargeImageListBean> large_image_list) {
            this.large_image_list = large_image_list;
        }

        public static class MiddleImageBean {
            /**
             * height : 196
             * uri : img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586
             * url : http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:300:196.webp?from=feed
             * url_list : [{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:300:196.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:300:196.webp?from=feed"}]
             * width : 300
             */

            private int height;
            private String uri;
            private String url;
            private int width;
            private List<UrlListBean> url_list;

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public String getUri() {
                return uri;
            }

            public void setUri(String uri) {
                this.uri = uri;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public List<UrlListBean> getUrl_list() {
                return url_list;
            }

            public void setUrl_list(List<UrlListBean> url_list) {
                this.url_list = url_list;
            }

            public static class UrlListBean {
                /**
                 * url : http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:300:196.webp?from=feed
                 */

                private String url;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }
        }

        public static class UserInfoBean {
            /**
             * avatar_url : http://sf6-ttcdn-tos.pstatp.com/img/pgc-image/719bfb75c812480d9ebf23009317ae0d~120x256.image
             * description : 这个人很懒，什么都没留下。
             * follow : false
             * follower_count : 0
             * home_page : http://open.toutiao.com/u63675336389/?utm_source=zx_rywnl_api&label=homepage&media_id=1659441453405198
             * media_id : 1659441453405198
             * name : 就要花里胡哨
             * user_id : 63675336389
             * user_verified : false
             */

            private String avatar_url;
            private String description;
            private boolean follow;
            private int follower_count;
            private String home_page;
            private long media_id;
            private String name;
            private long user_id;
            private boolean user_verified;

            public String getAvatar_url() {
                return avatar_url;
            }

            public void setAvatar_url(String avatar_url) {
                this.avatar_url = avatar_url;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public boolean isFollow() {
                return follow;
            }

            public void setFollow(boolean follow) {
                this.follow = follow;
            }

            public int getFollower_count() {
                return follower_count;
            }

            public void setFollower_count(int follower_count) {
                this.follower_count = follower_count;
            }

            public String getHome_page() {
                return home_page;
            }

            public void setHome_page(String home_page) {
                this.home_page = home_page;
            }

            public long getMedia_id() {
                return media_id;
            }

            public void setMedia_id(long media_id) {
                this.media_id = media_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public long getUser_id() {
                return user_id;
            }

            public void setUser_id(long user_id) {
                this.user_id = user_id;
            }

            public boolean isUser_verified() {
                return user_verified;
            }

            public void setUser_verified(boolean user_verified) {
                this.user_verified = user_verified;
            }
        }

        public static class CoverImageListBean {
            /**
             * height : 540
             * uri : img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586
             * url : http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:960:540.webp?from=feed
             * url_list : [{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:960:540.webp?from=feed"}]
             * width : 960
             */

            private int height;
            private String uri;
            private String url;
            private int width;
            private List<UrlListBeanX> url_list;

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public String getUri() {
                return uri;
            }

            public void setUri(String uri) {
                this.uri = uri;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public List<UrlListBeanX> getUrl_list() {
                return url_list;
            }

            public void setUrl_list(List<UrlListBeanX> url_list) {
                this.url_list = url_list;
            }

            public static class UrlListBeanX {
                /**
                 * url : http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:960:540.webp?from=feed
                 */

                private String url;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }
        }

        public static class FilterWordsBean {
            /**
             * id : 8:0
             * is_selected : false
             * name : 看过了
             */

            private String id;
            private boolean is_selected;
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public boolean isIs_selected() {
                return is_selected;
            }

            public void setIs_selected(boolean is_selected) {
                this.is_selected = is_selected;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class LargeImageListBean {
            /**
             * height : 540
             * uri : img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586
             * url : http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:960:540.webp?from=feed
             * url_list : [{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:960:540.webp?from=feed"},{"url":"http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:960:540.webp?from=feed"}]
             * width : 960
             */

            private int height;
            private String uri;
            private String url;
            private int width;
            private List<UrlListBeanXX> url_list;

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public String getUri() {
                return uri;
            }

            public void setUri(String uri) {
                this.uri = uri;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public List<UrlListBeanXX> getUrl_list() {
                return url_list;
            }

            public void setUrl_list(List<UrlListBeanXX> url_list) {
                this.url_list = url_list;
            }

            public static class UrlListBeanXX {
                /**
                 * url : http://p6-tt-ipv6.byteimg.com/img/pgc-image/ce168e05fa0e4faa9ae7572d5a5fa586~tplv-tt-cs0:960:540.webp?from=feed
                 */

                private String url;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }
        }

//        public NewsEntity getAdEntity() {
//            return adEntity;
//        }
//
//        public void setAdEntity(NewsEntity adEntity) {
//            this.adEntity = adEntity;
//        }

        public String getAdSlot() {
            return adSlot;
        }

        public void setAdSlot(String adSlot) {
            this.adSlot = adSlot;
        }

        public int getAdLoadState() {
            return adLoadState;
        }

        public void setAdLoadState(int adLoadState) {
            this.adLoadState = adLoadState;
        }
    }
}
