CREATE DATABASE  IF NOT EXISTS `shoppingstore` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `shoppingstore`;
-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: shoppingstore
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS `cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart` (
  `cid` int NOT NULL AUTO_INCREMENT COMMENT '购物车ID，主键自增长',
  `uid` int NOT NULL COMMENT '关联用户ID，外键（关联user表的uid）',
  `gid` int NOT NULL COMMENT '关联商品ID，外键（关联goods表的gid）',
  `num` int NOT NULL DEFAULT '1' COMMENT '商品数量，默认1',
  `goodsName` varchar(200) NOT NULL DEFAULT '' COMMENT '商品名称',
  `userName` varchar(50) NOT NULL DEFAULT '' COMMENT '用户名',
  PRIMARY KEY (`cid`),
  UNIQUE KEY `uid_gid_unique` (`uid`,`gid`),
  KEY `gid` (`gid`),
  CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`) ON DELETE CASCADE,
  CONSTRAINT `cart_ibfk_2` FOREIGN KEY (`gid`) REFERENCES `goods` (`gid`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='购物车信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `goods`
--

DROP TABLE IF EXISTS `goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `goods` (
  `gid` int NOT NULL AUTO_INCREMENT COMMENT '商品ID，主键自增长',
  `gname` varchar(100) NOT NULL COMMENT '商品名称，非空',
  `price` decimal(10,2) NOT NULL COMMENT '商品价格，保留2位小数',
  `stock` int NOT NULL DEFAULT '0' COMMENT '商品库存，默认0',
  `gdesc` varchar(255) DEFAULT NULL COMMENT '商品描述，可为空',
  `gimg` varchar(255) DEFAULT 'default_goods.jpg' COMMENT '商品图片路径',
  PRIMARY KEY (`gid`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order` (
  `oid` int NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `uid` int NOT NULL COMMENT '用户id',
  `totalPrice` decimal(10,2) NOT NULL COMMENT '订单总价',
  `address` varchar(255) NOT NULL COMMENT '收货地址',
  `status` int DEFAULT '0' COMMENT '0待付款 1待发货 2已完成',
  `orderTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  `goodsName` varchar(500) DEFAULT '' COMMENT '购买的商品名称',
  `username` varchar(50) DEFAULT '' COMMENT '用户名',
  PRIMARY KEY (`oid`),
  KEY `uid` (`uid`),
  CONSTRAINT `order_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `uid` int NOT NULL AUTO_INCREMENT COMMENT '用户ID，主键自增长',
  `username` varchar(50) NOT NULL COMMENT '用户名，非空且唯一（不可重复注册）',
  `password` varchar(50) NOT NULL COMMENT '用户密码，非空',
  `address` varchar(255) DEFAULT NULL COMMENT '用户地址，可为空',
  `phone` varchar(20) DEFAULT NULL COMMENT '用户手机号',
  `type` tinyint NOT NULL DEFAULT '0' COMMENT '用户类型：0=普通用户，1=管理员，默认普通用户',
  `state` int DEFAULT '1' COMMENT '用户状态 1正常 0禁用',
  `regTime` varchar(30) DEFAULT NULL COMMENT '注册时间',
  `status` int DEFAULT '1' COMMENT '用户状态：1=正常，0=禁用',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息表（含权限区分）';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-06 15:56:59
