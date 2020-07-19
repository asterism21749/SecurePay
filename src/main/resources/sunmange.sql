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

 Date: 20/07/2020 00:49:55
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

SET FOREIGN_KEY_CHECKS = 1;
