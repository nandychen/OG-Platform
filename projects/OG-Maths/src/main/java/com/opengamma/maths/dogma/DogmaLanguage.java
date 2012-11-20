// Autogenerated, do not edit!
/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.maths.dogma;
import com.opengamma.maths.commonapi.numbers.ComplexType;
import com.opengamma.maths.dogma.engine.language.InfixOperator;
import com.opengamma.maths.dogma.engine.language.UnaryFunction;
import com.opengamma.maths.dogma.engine.language.Function;
import com.opengamma.maths.dogma.engine.operationstack.InfixOpChain;
import com.opengamma.maths.dogma.engine.operationstack.MethodScraperForInfixOperators;
import com.opengamma.maths.dogma.engine.operationstack.MethodScraperForUnaryFunctions;
import com.opengamma.maths.dogma.engine.operationstack.OperatorDictionaryPopulator;
import com.opengamma.maths.dogma.engine.operationstack.RunInfixOpChain;
import com.opengamma.maths.dogma.engine.operationstack.RunUnaryFunctionChain;
import com.opengamma.maths.dogma.engine.operationstack.UnaryFunctionChain;
import com.opengamma.maths.highlevelapi.datatypes.primitive.OGArray;
import com.opengamma.maths.highlevelapi.datatypes.primitive.OGComplexScalar;
import com.opengamma.maths.highlevelapi.datatypes.primitive.OGMatrix;
import com.opengamma.maths.dogma.engine.matrixinfo.ConversionCostAdjacencyMatrixStore;
import com.opengamma.maths.highlevelapi.datatypes.primitive.OGRealScalar;
import com.opengamma.maths.highlevelapi.datatypes.primitive.OGMatrix;
import com.opengamma.maths.dogma.engine.matrixinfo.MatrixTypeToIndexMap;
import com.opengamma.maths.dogma.engine.methodhookinstances.Sin;
import com.opengamma.maths.dogma.engine.methodhookinstances.Plus;
import com.opengamma.maths.dogma.engine.methodhookinstances.Mtimes;
/**
 * Provides the DOGMA Language
 */
public class DogmaLanguage {
private static DogmaLanguage s_instance;
DogmaLanguage() {
}
public static DogmaLanguage getInstance() {
return s_instance;
}
private static RunInfixOpChain s_infixOpChainRunner = new RunInfixOpChain();
private static RunUnaryFunctionChain s_unaryFunctionChainRunner = new RunUnaryFunctionChain();
private static UnaryFunctionChain[] s_sinInstructions; //CSOFF
private static InfixOpChain[][] s_plusInstructions; //CSOFF
private static InfixOpChain[][] s_mtimesInstructions; //CSOFF
static {
final double[][] DefaultInfixFunctionEvalCosts = new double[][] {//
{1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },//
{0, 1, 0, 0, 0, 0, 0, 0, 0, 0 },//
{0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },//
{0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },//
{0, 0, 0, 0, 1, 0, 0, 0, 0, 0 },//
{0, 0, 0, 0, 0, 1, 0, 0, 0, 0 },//
{0, 0, 0, 0, 0, 0, 25, 0, 50, 0 },//
{0, 0, 0, 0, 0, 0, 0, 1, 0, 0 },//
{0, 0, 0, 0, 0, 0, 50, 0, 100, 200 },//
{0, 0, 0, 0, 0, 0, 0, 0, 0, 200 } };
OGMatrix defaultInfixFunctionEvalCostsMatrix = new OGMatrix(DefaultInfixFunctionEvalCosts);
final double[][] DefaultUnaryFunctionEvalCosts = new double[][] {//
{1 },//
{1 },//
{2 },//
{3 },//
{3 },//
{5 },//
{5 },//
{5 },//
{10 },//
{20 } };
OGMatrix defaultUnaryFunctionEvalCostsMatrix = new OGMatrix(DefaultUnaryFunctionEvalCosts);
// Build instructions sets
 OperatorDictionaryPopulator<InfixOperator<OGArray<? extends Number>, OGArray<? extends Number>, OGArray<? extends Number>>> operatorDictInfix = new OperatorDictionaryPopulator<InfixOperator<OGArray<? extends Number>, OGArray<? extends Number>, OGArray<? extends Number>>>();
OperatorDictionaryPopulator<UnaryFunction<OGArray<? extends Number>, OGArray<? extends Number>>> operatorDictUnary = new OperatorDictionaryPopulator<UnaryFunction<OGArray<? extends Number>, OGArray<? extends Number>>>();
UnaryFunction<OGArray<? extends Number>, OGArray<? extends Number>>[] SinFunctionTable = MethodScraperForUnaryFunctions.availableMethodsForUnaryFunctions(operatorDictUnary.getOperationsMap(),Sin.class);
s_sinInstructions = MethodScraperForUnaryFunctions.computeFunctions(ConversionCostAdjacencyMatrixStore.getWeightedAdjacencyMatrix(),SinFunctionTable, defaultUnaryFunctionEvalCostsMatrix);

InfixOperator<OGArray<? extends Number>, OGArray<? extends Number>, OGArray<? extends Number>>[][] PlusFunctionTable = MethodScraperForInfixOperators.availableMethodsForInfixOp(operatorDictInfix.getOperationsMap(),Plus.class);
s_plusInstructions = MethodScraperForInfixOperators.computeFunctions(ConversionCostAdjacencyMatrixStore.getWeightedAdjacencyMatrix(),PlusFunctionTable, defaultInfixFunctionEvalCostsMatrix);

InfixOperator<OGArray<? extends Number>, OGArray<? extends Number>, OGArray<? extends Number>>[][] MtimesFunctionTable = MethodScraperForInfixOperators.availableMethodsForInfixOp(operatorDictInfix.getOperationsMap(),Mtimes.class);
s_mtimesInstructions = MethodScraperForInfixOperators.computeFunctions(ConversionCostAdjacencyMatrixStore.getWeightedAdjacencyMatrix(),MtimesFunctionTable, defaultInfixFunctionEvalCostsMatrix);


}

public static OGArray<? extends Number>sin(OGArray<? extends Number> arg1) {int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg1.getClass());OGArray<? extends Number> tmp = s_unaryFunctionChainRunner.dispatch(s_sinInstructions[type1], arg1);return tmp;}

public static OGArray<? extends Number>sin(Number arg1) {OGArray<? extends Number> arg1rewrite;if (arg1.getClass() == ComplexType.class) {
arg1rewrite = new OGComplexScalar(arg1);
} else {
arg1rewrite = new OGRealScalar(arg1);
}
int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg1rewrite.getClass());
OGArray<? extends Number> tmp = s_unaryFunctionChainRunner.dispatch(s_sinInstructions[type1], arg1rewrite);return tmp;}


public static OGArray<? extends Number>plus(OGArray<? extends Number> arg1, OGArray<? extends Number> arg2) {
  int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg1.getClass());
  int type2 = MatrixTypeToIndexMap.getIndexFromClass(arg2.getClass());
  OGArray<? extends Number> tmp = s_infixOpChainRunner.dispatch(s_plusInstructions[type1][type2], arg1, arg2);
  return tmp;
}

public static OGArray<? extends Number> plus(Number arg1, OGArray<? extends Number> arg2) {
OGArray<? extends Number> arg1rewrite;
if (arg1.getClass() == ComplexType.class) {
arg1rewrite = new OGComplexScalar(arg1);
} else {
arg1rewrite = new OGRealScalar(arg1);
}
int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg1rewrite.getClass());
int type2 = MatrixTypeToIndexMap.getIndexFromClass(arg2.getClass());
  OGArray<? extends Number> tmp = s_infixOpChainRunner.dispatch(s_plusInstructions[type1][type2], arg1rewrite, arg2);
  return tmp;
}

public static OGArray<? extends Number>plus(OGArray<? extends Number> arg1, Number arg2) {
OGArray<? extends Number> arg2rewrite;
if (arg2.getClass() == ComplexType.class) {
arg2rewrite = new OGComplexScalar(arg2);
} else {
 arg2rewrite = new OGRealScalar(arg2);
 }
 int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg2rewrite.getClass());
int type2 = MatrixTypeToIndexMap.getIndexFromClass(arg1.getClass());
  OGArray<? extends Number> tmp = s_infixOpChainRunner.dispatch(s_plusInstructions[type1][type2], arg1, arg2rewrite);
  return tmp;
}


public static OGArray<? extends Number>mtimes(OGArray<? extends Number> arg1, OGArray<? extends Number> arg2) {
  int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg1.getClass());
  int type2 = MatrixTypeToIndexMap.getIndexFromClass(arg2.getClass());
  OGArray<? extends Number> tmp = s_infixOpChainRunner.dispatch(s_mtimesInstructions[type1][type2], arg1, arg2);
  return tmp;
}

public static OGArray<? extends Number> mtimes(Number arg1, OGArray<? extends Number> arg2) {
OGArray<? extends Number> arg1rewrite;
if (arg1.getClass() == ComplexType.class) {
arg1rewrite = new OGComplexScalar(arg1);
} else {
arg1rewrite = new OGRealScalar(arg1);
}
int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg1rewrite.getClass());
int type2 = MatrixTypeToIndexMap.getIndexFromClass(arg2.getClass());
  OGArray<? extends Number> tmp = s_infixOpChainRunner.dispatch(s_mtimesInstructions[type1][type2], arg1rewrite, arg2);
  return tmp;
}

public static OGArray<? extends Number>mtimes(OGArray<? extends Number> arg1, Number arg2) {
OGArray<? extends Number> arg2rewrite;
if (arg2.getClass() == ComplexType.class) {
arg2rewrite = new OGComplexScalar(arg2);
} else {
 arg2rewrite = new OGRealScalar(arg2);
 }
 int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg2rewrite.getClass());
int type2 = MatrixTypeToIndexMap.getIndexFromClass(arg1.getClass());
  OGArray<? extends Number> tmp = s_infixOpChainRunner.dispatch(s_mtimesInstructions[type1][type2], arg1, arg2rewrite);
  return tmp;
}


}
