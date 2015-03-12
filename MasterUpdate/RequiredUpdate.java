package com.kth.csd.networking;

import java.util.HashMap;

public class Commands {
 public enum Operations {
  UPDATE, DELETE, INSERT
  
 }
 private HashMap<Integer, Double> keyValue; 
 private Operations operations;
 
 public Commands(HashMap<Integer, Double> keyValue){
  this.keyValue = keyValue;
  
 }
 public Commands(HashMap<Integer, Double> keyValue,Operations operations){
  this.keyValue = keyValue;
  this.operations = operations;
 }
 public Commands(Operations operations){
  this.operations = operations;
  
 }
 public HashMap<Integer, Double> getKeyValue() {
  return keyValue;
 }
 public Operations getOperations() {
  return operations;
 }
 
 
 

}