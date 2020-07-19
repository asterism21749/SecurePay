/*
 Navicat MySQL Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : localhost:3306
 Source Schema         : sunmange

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 17/07/2020 21:19:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for unpy_ipft
-- ----------------------------
DROP TABLE IF EXISTS `unpy_ipft`;
CREATE TABLE `unpy_ipft`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mark` int(1) NULL DEFAULT NULL,
  `ctime` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `utime` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of unpy_ipft
-- ----------------------------
INSERT INTO `unpy_ipft` VALUES (1, '192.168.0.230', 'unpy', 0, '2020-07-17 13:31:28', '2020-07-17 16:18:57', NULL);

-- ----------------------------
-- Table structure for unpy_logs
-- ----------------------------
DROP TABLE IF EXISTS `unpy_logs`;
CREATE TABLE `unpy_logs`  (
  `fcbpdt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '日期',
  `fcbpsq` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流水',
  `orderId` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `trantm` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '交易发生时间',
  `endtime` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '交易结束时间',
  `rqdata` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '接受的请求报文',
  `rpdata` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '返回的报文',
  `respcd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '返回码',
  `resptx` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '返回信息',
  PRIMARY KEY (`fcbpsq`, `fcbpdt`) USING BTREE,
  UNIQUE INDEX `unique_index`(`fcbpdt`, `fcbpsq`) USING BTREE,
  INDEX `query_index`(`fcbpdt`) USING BTREE,
  UNIQUE INDEX `orderId`(`orderId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '交易日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of unpy_logs
-- ----------------------------
INSERT INTO `unpy_logs` VALUES ('20200717', '000000000001', '1234567890', '2020-07-17 20:53:33', NULL, '{\"orderId\":\"1234567890\",\"txnAmt\":\"60.00\",\"orderDesc\":\"普通订单\"}', NULL, NULL, NULL);
INSERT INTO `unpy_logs` VALUES ('20200717', '000000000002', '1234567891', '2020-07-17 21:09:48', NULL, '{\"orderId\":\"1234567891\",\"txnAmt\":\"60.00\",\"orderDesc\":\"普通订单\"}', NULL, NULL, NULL);
INSERT INTO `unpy_logs` VALUES ('20200717', '000000000003', '1234567893', '2020-07-17 21:13:58', NULL, '{\"orderId\":\"1234567893\",\"txnAmt\":\"60.00\",\"orderDesc\":\"普通订单\"}', NULL, NULL, NULL);
INSERT INTO `unpy_logs` VALUES ('20200717', '000000000004', '1234567894', '2020-07-17 21:14:42', '2020-07-17 21:15:03', '{\"orderId\":\"1234567894\",\"txnAmt\":\"60.00\",\"orderDesc\":\"普通订单\"}', '<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/></head><body><form id=\"pay_form\" action=\"http://localhost:8080/test\" method=\"post\"><input type=\"hidden\" name=\"respcd\" id=\"respcd\" value=\"0000\"/><input type=\"hidden\" name=\"resptx\" id=\"resptx\" value=\"success\"/><input type=\"hidden\" name=\"orderId\" id=\"orderId\" value=\"1234567894\"/><input type=\"hidden\" name=\"txnTime\" id=\"txnTime\" value=\"20200717211442\"/><input type=\"hidden\" name=\"txnAmt\" id=\"txnAmt\" value=\"6000\"/></form></body><script type=\"text/javascript\">document.all.pay_form.submit();</script></html>', '0000', 'success');
INSERT INTO `unpy_logs` VALUES ('20200717', '000000000005', '1234567899', '2020-07-17 21:17:36', '2020-07-17 21:18:02', '{\"orderId\":\"1234567899\",\"txnAmt\":\"60.00\",\"orderDesc\":\"普通订单\"}', '<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/></head><body><form id=\"pay_form\" action=\"http://localhost:8080/test\" method=\"post\"><input type=\"hidden\" name=\"respcd\" id=\"respcd\" value=\"0000\"/><input type=\"hidden\" name=\"resptx\" id=\"resptx\" value=\"success\"/><input type=\"hidden\" name=\"orderId\" id=\"orderId\" value=\"1234567899\"/><input type=\"hidden\" name=\"txnTime\" id=\"txnTime\" value=\"20200717211736\"/><input type=\"hidden\" name=\"txnAmt\" id=\"txnAmt\" value=\"6000\"/></form></body><script type=\"text/javascript\">document.all.pay_form.submit();</script></html>', '0000', 'success');

-- ----------------------------
-- Table structure for unpy_para
-- ----------------------------
DROP TABLE IF EXISTS `unpy_para`;
CREATE TABLE `unpy_para`  (
  `subsys` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `chanel` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `descpn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `param1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `param2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `param3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `param4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `param5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `param6` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`subsys`, `chanel`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of unpy_para
-- ----------------------------
INSERT INTO `unpy_para` VALUES ('unionpay', 'SecurePay', '商户号|第三方接口地址|IIN码', '709034470110011', 'http://localhost:8080/test', '01040826', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for unpy_tran
-- ----------------------------
DROP TABLE IF EXISTS `unpy_tran`;
CREATE TABLE `unpy_tran`  (
  `fcbpdt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日期',
  `fcbpsq` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流水',
  `version` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '版本号',
  `encoding` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编码方式',
  `certId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '证书序列号',
  `signature` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '签名',
  `signMethod` int(2) NULL DEFAULT NULL COMMENT '01:RSA',
  `txnType` int(2) NULL DEFAULT NULL COMMENT '01:消费',
  `txnSubType` int(2) NULL DEFAULT NULL,
  `bizType` int(6) NULL DEFAULT NULL COMMENT '业务种类',
  `accessType` int(1) NULL DEFAULT NULL,
  `channelType` int(2) NULL DEFAULT NULL COMMENT '渠道类型 07:Internet 08:Mobile',
  `frontUrl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `backUrl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `merId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `merCatCode` int(4) NULL DEFAULT NULL,
  `merName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `merAbbr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `orderId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `txnTime` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交易时间',
  `accNo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `txnAmt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `currencyCode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `customerInfo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `frontFailUrl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `orderDesc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tokenPayData` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `respCode` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '返回码',
  `respMsg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '返回信息',
  `settleDate` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '清算日期',
  `settleCurrencyCode` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `settleAmt` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `traceNo` int(6) NULL DEFAULT NULL COMMENT '系统跟踪审核编号',
  `queryId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `traceTime` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `transt` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '交易状态 0:初始状态 1:成功 2:失败 3:银联未知（未收到backRcvResponse）',
  UNIQUE INDEX `orderId`(`orderId`) USING BTREE,
  INDEX `fcbpdt`(`fcbpdt`, `fcbpsq`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of unpy_tran
-- ----------------------------
INSERT INTO `unpy_tran` VALUES ('20200717', '000000000001', '5.1.0', 'UTF-8', '69629715588', 'DXPHEkZXfLOtvI9VuMppSp+Kn7kZ1gqAggkyjQuDH2o2Qmld6CEX9jpMiGZ4zDBYaUkbM2si3OQrM1qQMwxffMJKLBi3dNoEC9a0tSVlQAa4ZagCJChrolEGLmveMI5/YH5sasEdNMGKy1x/uxKPSlPsrJDuku+iBA+Uvuz0gQIauB1+JCc0pMDT46+F/RcTagTHSho8Q09E54bNJlg5ZRBkUqjTlUn2gCOF+P15o7sz/J5TYsOEYb1ulGD3SaUBl6kAQrI0Rzv9LOWXvNQ3eAPQoDZP7L6EXv+48ukGDYTd1jTsGrZ28/kOp/mbYjVI/mdcY6nISVsBwu1yMBrkeQ==', 1, 1, 1, 201, 0, 7, 'http://localhost:9445/frontRcvResponse', 'http://localhost:9445/backRcvResponse', '709034470110011', NULL, NULL, NULL, '1234567890', '20200717205333', NULL, '6000.0', '156', NULL, NULL, '普通订单', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `unpy_tran` VALUES ('20200717', '000000000002', '5.1.0', 'UTF-8', '69629715588', 'DOI4PBCigFHD123dPDlod5GwPAhyCDnbCkaUDT4k8Hu5qqq/3521aRiVUAZdtCfINNLjdevXyY9xbb5mU1q1FhHXjJ+G/1oMw+KOfgwNW5m8I3z8L9hPJJN7tX0U1lfrqqN22aXW+w1z0Zu/gjIUFEDPp6rTaKEa4CGnrA42BU0tp+t5vxSCFcSNaCr4ubS5qNYqPPIczP/v0QL0TGLtJXJOeYxT4OH3bPWXaH8nME2zfYoFKLnuUL+EFs7fOjWi4dwRCLvOXmEuVrOMeiDuxhzwqS+gJxqLrU3I7+NNVv03lmWvXi2Uyd6FzvdZJuGZzf+CzdlQFT46VNVTOBXhPQ==', 1, 1, 1, 201, 0, 7, 'http://localhost:9445/frontRcvResponse', 'http://localhost:9445/backRcvResponse', '709034470110011', NULL, NULL, NULL, '1234567891', '20200717210948', NULL, '60.00', '156', NULL, NULL, '普通订单', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `unpy_tran` VALUES ('20200717', '000000000003', '5.1.0', 'UTF-8', '69629715588', 'oWu+yeoy/498Q7Ta4EH8AIFYoEX860rdEBkJ4yCLKtUBPZF7n+aZfL6scDOoBRpkoab/Ce61GOohAcWOVUog+ttO8nYjVQeGtuReZrE3GnYCIolyj+Wfy1yqaVopnSTF2UDQi9VYfhloEuZNviIRexOXre5dhCNBIUKMPIHKt9vbeOGfTQrmcLi0xYFmZ/bXfmWGOBKE2QBW/3HDVJxe0g97J7ie1mNpVmich3nsk7eeQn00tL3anm8Yo94kM+XaQKmKH4S8med0/OFrAu3RIzB5i0PgmC/B47iDgqMApsawpY0+kcBGsq6mGn73xOjOrsVIy7eKdvD53CBISO22nA==', 1, 1, 1, 201, 0, 7, 'http://localhost:9445/frontRcvResponse', 'http://localhost:9445/backRcvResponse', '709034470110011', NULL, NULL, NULL, '1234567893', '20200717211358', NULL, '6000.00', '156', NULL, NULL, '普通订单', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0');
INSERT INTO `unpy_tran` VALUES ('20200717', '000000000004', '5.1.0', 'UTF-8', '69629715588', 'Sc7Zi7qpTBBNq1hJdMkxVUrvZgkVsJ7vYe/4/GJl5wL7/FfTIWtxv6foUu9cJO8IRaM+HLYvflIzILeB1kyEYBI3s+GBHcP+Do1Rd+ZwbNRB95LAovZHnak06AyCzgSwi34bYO7QEhHxZYarsJrRgWZHB6f1vV12z4Rjp/NNZ3S6WwSJbCTD0PCnxtGkU3/SrVnEzz23ZhwKPHA1yBWy/dI7We3RUmqkbK1PVntDg/Gs5pafpajCtpIL/MBizGoGS5PbZ4XKhv2wQZidJ79Gd1xxMYJ/sQhCh7dtcT1OBjW8W/vBxVe3BcF5jj7EVAH9NF3xH4/38Eq4sdOcubry3g==', 1, 1, 1, 201, 0, 7, 'http://localhost:9445/frontRcvResponse', 'http://localhost:9445/backRcvResponse', '709034470110011', NULL, NULL, NULL, '1234567894', '20200717211442', NULL, '6000', '156', NULL, NULL, '普通订单', NULL, '00', 'success', '0717', '156', '6000', 346150, '832007172114423461508', '0717211442', '3');
INSERT INTO `unpy_tran` VALUES ('20200717', '000000000005', '5.1.0', 'UTF-8', '69629715588', 'QjcTiFTHVmTnIofHbfJVHy6tRgTO+7dnMRAQDAookpsEMaBJ14V2XMxsn0vVRMzpUKTQCmUSGhWJw5qRZ+iL88VFXW7Kc9IcOOFExiNTMJGv1JamonoJO71pWhpmiGtVTftn25jeJf0LPp4CVlILzlZXJ+17yeRi/DkrykO+u9W29+tI5Q4gz/C92mz9Xafuqsk5E40RUumspmbM5B62QZwPb16D8gK0/YztHlKlbSUijo6WAZUZmANnjaLB2m1JSov0EBPnzyuGrwQBZRDWYMg0ImWn69pgRGQFKAxMw1JcC4hB2tTYV8gAveAxmWEo32Ta0r2SqBn9eNMyQUyh7w==', 1, 1, 1, 201, 0, 7, 'http://localhost:9445/frontRcvResponse', 'http://localhost:9445/backRcvResponse', '709034470110011', NULL, NULL, NULL, '1234567899', '20200717211736', NULL, '6000', '156', NULL, NULL, '普通订单', NULL, '00', 'success', '0717', '156', '6000', 346400, '502007172117363464008', '0717211736', '3');

SET FOREIGN_KEY_CHECKS = 1;
