package com.autumn.framework.user;

public class level_show_manager {

    public static int get_hg(String grow_integrals){
        double level = get_level(grow_integrals)-53;
        double same_one = 54;
        if (get_level(grow_integrals) <= 53){
            return 0;
        }else if (Math.ceil(level/same_one) == 1){
            return 1;
        }else{
            return 2;
        }
    }

    public static int get_ty(String grow_integrals){
        double level = get_level(grow_integrals)-17;
        double same_one = 18;
        if (get_level(grow_integrals) <= 17 || get_level(grow_integrals) == 109){
            return 0;
        }else if (Math.ceil(level/same_one) % 3 == 0){
            return 0;
        }else if (Math.ceil(level/same_one) % 3 == 1){
            return 1;
        }else if (Math.ceil(level/same_one) % 3 == 2){
            return 2;
        }else{
            return 0;
        }
    }

    public static int get_yl(String grow_integrals){
        double level = get_level(grow_integrals)-5;
        double same_one = 6;
        //LogUtil.i(level+"");
        //LogUtil.i(String.valueOf(level/same_one));
        if (get_level(grow_integrals) <= 5 || get_level(grow_integrals) == 109){
            return 0;
        }else if (Math.ceil(level/same_one) % 3 == 0){
            return 0;
        }else if (Math.ceil(level/same_one) % 3 == 1){
            return 1;
        }else if (Math.ceil(level/same_one) % 3 == 2){
            return 2;
        }else{
            return 0;
        }
    }

    public static int get_qxx(String grow_integrals){
        //LogUtil.i(String.valueOf(get_level(grow_integrals)));
        if (get_level(grow_integrals) == 109){
            return 0;
        }else {
            if (get_level(grow_integrals) % 6 == 1) {
                return 0;
            } else if (get_level(grow_integrals) % 6 == 2 || get_level(grow_integrals) % 6 == 3) {
                return 1;
            } else if (get_level(grow_integrals) % 6 == 4 || get_level(grow_integrals) % 6 == 5) {
                return 2;
            } else {
                return 0;
            }
        }
    }

    public static int get_bxx(String grow_integrals){
        if (get_level(grow_integrals) == 109){
            return 0;
        }else {
            if (get_level(grow_integrals) % 2 == 0) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    public static int get_level(String grow_integrals){

        int grow_num = Integer.valueOf(grow_integrals);

        if (0 <= grow_num && grow_num < 10){
            //10
            return 1;
        }else if (10 <= grow_num && grow_num < 30){
            //20
            return 2;
        }else if (30 <= grow_num && grow_num < 60){
            //30
            return 3;
        }else if (60 <= grow_num && grow_num < 100){
            //40
            return 4;
        }else if (100 <= grow_num && grow_num < 150){
            //50
            return 5;
        }else if (150 <= grow_num && grow_num < 210){
            //60
            return 6;
        }else if (210 <= grow_num && grow_num < 280){
            //70
            return 7;
        }else if (280 <= grow_num && grow_num < 360){
            //80
            return 8;
        }else if (360 <= grow_num && grow_num < 450){
            //90
            return 9;
        }else if (450 <= grow_num && grow_num < 550){
            //100
            return 10;
        }else if (550 <= grow_num && grow_num < 660){
            //110
            return 11;
        }else if (660 <= grow_num && grow_num < 780){
            //120
            return 12;
        }else if (780 <= grow_num && grow_num < 910){
            //130
            return 13;
        }else if (910 <= grow_num && grow_num < 1050){
            //140
            return 14;
        }else if (1050 <= grow_num && grow_num < 1200){
            //150
            return 15;
        }else if (1200 <= grow_num && grow_num < 1360){
            //160
            return 16;
        }else if (1360 <= grow_num && grow_num < 1530){
            //170
            return 17;
        }else if (1530 <= grow_num && grow_num < 1710){
            //180
            return 18;
        }else if (1710 <= grow_num && grow_num < 1900){
            //190
            return 19;
        }else if (1900 <= grow_num && grow_num < 2100){
            //200
            return 20;
        }else if (2100 <= grow_num && grow_num < 2310){
            //210
            return 21;
        }else if (2310 <= grow_num && grow_num < 2530){
            //220
            return 22;
        }else if (2530 <= grow_num && grow_num < 2760){
            //230
            return 23;
        }else if (2760 <= grow_num && grow_num < 3000){
            //240
            return 24;
        }else if (3000 <= grow_num && grow_num < 3250){
            //250
            return 25;
        }else if (3250 <= grow_num && grow_num < 3510){
            //260
            return 26;
        }else if (3510 <= grow_num && grow_num < 3780){
            //270
            return 27;
        }else if (3780 <= grow_num && grow_num < 4060){
            //280
            return 28;
        }else if (4060 <= grow_num && grow_num < 4350){
            //290
            return 29;
        }else if (4350 <= grow_num && grow_num < 4650){
            //300
            return 30;
        }else if (4650 <= grow_num && grow_num < 4960){
            //310
            return 31;
        }else if (4960 <= grow_num && grow_num < 5280){
            //320
            return 32;
        }else if (5280 <= grow_num && grow_num < 5610){
            //330
            return 33;
        }else if (5610 <= grow_num && grow_num < 5950){
            //340
            return 34;
        }else if (5950 <= grow_num && grow_num < 6300){
            //350
            return 35;
        }else if (6300 <= grow_num && grow_num < 6660){
            //360
            return 36;
        }else if (6660 <= grow_num && grow_num < 7030){
            //370
            return 37;
        }else if (7030 <= grow_num && grow_num < 7410){
            //380
            return 38;
        }else if (7410 <= grow_num && grow_num < 7800){
            //390
            return 39;
        }else if (7800 <= grow_num && grow_num < 8200){
            //400
            return 40;
        }else if (8200 <= grow_num && grow_num < 8610){
            //410
            return 41;
        }else if (8610 <= grow_num && grow_num < 9030){
            //420
            return 42;
        }else if (9030 <= grow_num && grow_num < 9460){
            //430
            return 43;
        }else if (9460 <= grow_num && grow_num < 9900){
            //440
            return 44;
        }else if (9900 <= grow_num && grow_num < 10350){
            //450
            return 45;
        }else if (10350 <= grow_num && grow_num < 10810){
            //460
            return 46;
        }else if (10810 <= grow_num && grow_num < 11280){
            //470
            return 47;
        }else if (11280 <= grow_num && grow_num < 11760){
            //480
            return 48;
        }else if (11760 <= grow_num && grow_num < 12250){
            //490
            return 49;
        }else if (12250 <= grow_num && grow_num < 12750){
            //500
            return 50;
        }else if (12750 <= grow_num && grow_num < 13260){
            //510
            return 51;
        }else if (13260 <= grow_num && grow_num < 13780){
            //520
            return 52;
        }else if (13780 <= grow_num && grow_num < 14310){
            //530
            return 53;
        }else if (14310 <= grow_num && grow_num < 14850){
            //540
            return 54;
        }else if (14850 <= grow_num && grow_num < 15400){
            //550
            return 55;
        }else if (15400 <= grow_num && grow_num < 15960){
            //560
            return 56;
        }else if (15960 <= grow_num && grow_num < 16530){
            //570
            return 57;
        }else if (16530 <= grow_num && grow_num < 17110){
            //580
            return 58;
        }else if (17110 <= grow_num && grow_num < 17700){
            //590
            return 59;
        }else if (17700 <= grow_num && grow_num < 18300){
            //600
            return 60;
        }else if (18300 <= grow_num && grow_num < 18910){
            //610
            return 61;
        }else if (18910 <= grow_num && grow_num < 19530){
            //620
            return 62;
        }else if (19530 <= grow_num && grow_num < 20160){
            //630
            return 63;
        }else if (20160 <= grow_num && grow_num < 20800){
            //640
            return 64;
        }else if (20800 <= grow_num && grow_num < 21450){
            //650
            return 65;
        }else if (21450 <= grow_num && grow_num < 22110){
            //660
            return 66;
        }else if (22110 <= grow_num && grow_num < 22780){
            //670
            return 67;
        }else if (22780 <= grow_num && grow_num < 23460){
            //680
            return 68;
        }else if (23460 <= grow_num && grow_num < 24150){
            //690
            return 69;
        }else if (24150 <= grow_num && grow_num < 24850){
            //700
            return 70;
        }else if (24850 <= grow_num && grow_num < 25560){
            //710
            return 71;
        }else if (25560 <= grow_num && grow_num < 26280){
            //720
            return 72;
        }else if (26280 <= grow_num && grow_num < 27010){
            //730
            return 73;
        }else if (27010 <= grow_num && grow_num < 27750){
            //740
            return 74;
        }else if (27750 <= grow_num && grow_num < 28500){
            //750
            return 75;
        }else if (28500 <= grow_num && grow_num < 29260){
            //760
            return 76;
        }else if (29260 <= grow_num && grow_num < 30030){
            //770
            return 77;
        }else if (30030 <= grow_num && grow_num < 30810){
            //780
            return 78;
        }else if (30810 <= grow_num && grow_num < 31600){
            //790
            return 79;
        }else if (31600 <= grow_num && grow_num < 32400){
            //800
            return 80;
        }else if (32400 <= grow_num && grow_num < 33210){
            //810
            return 81;
        }else if (33210 <= grow_num && grow_num < 34030){
            //820
            return 82;
        }else if (34030 <= grow_num && grow_num < 34860){
            //830
            return 83;
        }else if (34860 <= grow_num && grow_num < 35700){
            //840
            return 84;
        }else if (35700 <= grow_num && grow_num < 36550){
            //850
            return 85;
        }else if (36550 <= grow_num && grow_num < 37410){
            //860
            return 86;
        }else if (37410 <= grow_num && grow_num < 38280){
            //870
            return 87;
        }else if (38280 <= grow_num && grow_num < 39160){
            //880
            return 88;
        }else if (39160 <= grow_num && grow_num < 40050){
            //890
            return 89;
        }else if (40050 <= grow_num && grow_num < 40950){
            //900
            return 90;
        }else if (40950 <= grow_num && grow_num < 41860){
            //910
            return 91;
        }else if (41860 <= grow_num && grow_num < 42780){
            //920
            return 92;
        }else if (42780 <= grow_num && grow_num < 43710){
            //930
            return 93;
        }else if (43710 <= grow_num && grow_num < 44650){
            //940
            return 94;
        }else if (44650 <= grow_num && grow_num < 45600){
            //950
            return 95;
        }else if (45600 <= grow_num && grow_num < 46560){
            //960
            return 96;
        }else if (46560 <= grow_num && grow_num < 47530){
            //970
            return 97;
        }else if (47530 <= grow_num && grow_num < 48510){
            //980
            return 98;
        }else if (48510 <= grow_num && grow_num < 49500){
            //990
            return 99;
        }else if (49500 <= grow_num && grow_num < 50500){
            //1000
            return 100;
        }else if (50500 <= grow_num && grow_num < 51510){
            //1010
            return 101;
        }else if (51510 <= grow_num && grow_num < 52530){
            //1020
            return 102;
        }else if (52530 <= grow_num && grow_num < 53560){
            //1030
            return 103;
        }else if (53560 <= grow_num && grow_num < 54600){
            //1040
            return 104;
        }else if (54600 <= grow_num && grow_num < 55650){
            //1050
            return 105;
        }else if (55650 <= grow_num && grow_num < 56710){
            //1060
            return 106;
        }else if (56710 <= grow_num && grow_num < 57780){
            //1070
            return 107;
        }else if (57780 <= grow_num && grow_num < 58860){
            //1080
            return 108;
        }else{
            return 109;
        }

    }

}
