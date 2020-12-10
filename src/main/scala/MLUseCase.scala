import org.apache.spark.ml.feature.{OneHotEncoder, PCA, StringIndexer, VectorAssembler}
import org.apache.spark.sql.{Row, SQLContext, SparkSession}
import org.apache.spark.{SparkConf, SparkContext, sql}
import org.apache.spark.sql.functions.{array, col, monotonically_increasing_id, regexp_replace, split, when}
import org.apache.spark.sql.types.{DoubleType, IntegerType, StructField}
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.mllib.classification.{NaiveBayes, NaiveBayesModel}
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.mllib.feature.{StandardScaler, StandardScalerModel}


object MLUseCase {
  var conf = new SparkConf().setAppName("Read CSV File").setMaster("local[*]")
  val sc = new SparkContext(conf)
  sc.setLogLevel("WARN")
  val sqlContext = new SQLContext(sc)
  val spark = SparkSession.builder().getOrCreate()
  import spark.implicits._ //
  def main(args: Array[String]): Unit = {

//    var conf = new SparkConf().setAppName("Read CSV File").setMaster("local[*]")
//    val sc = new SparkContext(conf)
//    sc.setLogLevel("WARN")
//    val sqlContext = new SQLContext(sc)
    var personDF = sqlContext.read.format("csv")
      .option("header", "true")
      .option("inferSchema", "true")
      .load("C:\\Users\\shrir\\OneDrive - Northeastern University\\CSYE7200 Big Data Systems Enginnering with " +
        "Scala - Project\\Data\\person\\*.csv")


    personDF = personDF.drop("CRASH_STATUS","POLC_AGNCY_TYPE_DESCR","VEHC_TRVL_DIRC_CL","MM_RTE","DIST_DIRC_MILEMARKER","MILEMARKER","EXIT_RTE","EXIT_NUMB","DIST_DIRC_LANDMARK","LANDMARK","X","Y","RMV_DOC_IDS","CRASH_RPT_IDS","RPA_ABBR","VEHC_EMER_USE_CL","VEHC_TOWED_FROM_SCENE_CL","FMCSA_RPTBL_CL","FMCSA_RPTBL","ROAD_CNTRB_DESCR","SCHL_BUS_RELD_DESCR","WORK_ZONE_RELD_DESCR","HAZ_MAT_PLACARD_DESCR","VEHC_REG_TYPE_CODE","NON_MTRST_TYPE_CL","NON_MTRST_ACTN_CL","NON_MTRST_LOC_CL","AADT","AADT_YEAR","PK_PCT_SUT","AV_PCT_SUT","PK_PCT_CT","AV_PCT_CT","CURB","TRUCK_RTE","LT_SIDEWLK","RT_SIDEWLK","SHLDR_LT_W","SHLDR_LT_T","SURFACE_WD","SHLDR_RT_W","SHLDR_RT_T","OPP_LANES","MED_WIDTH","MED_TYPE","URBAN_TYPE","F_CLASS","URBAN_AREA","FD_AID_RTE","FACILITY","OPERATION","CONTROL","PEAK_LANE","STREETNAME","FROMSTREETNAME","TOSTREETNAME","CITY","STRUCT_CND","TERRAIN","URBAN_LOC_TYPE","AADT_DERIV","STATN_NUM","OP_DIR_SL","SHLDR_UL_T","SHLDR_UL_W","T_EXC_TYPE","T_EXC_TIME","F_F_CLASS"
    )

    personDF.printSchema()

    println(personDF.count())


//    var VEHC_SEQ_EVENTS_DF = personDF.withColumn("VEHC_SEQ_EVENTS", split(col("VEHC_SEQ_EVENTS"), "\\SEQ")).select(
//      col("VEHC_SEQ_EVENTS").getItem(0).as("col1"),
//      col("VEHC_SEQ_EVENTS").getItem(1).as("col2"),
//      col("VEHC_SEQ_EVENTS").getItem(2).as("col3")
//    )
//    personDF = personDF.withColumn("id", monotonically_increasing_id())
//    VEHC_SEQ_EVENTS_DF = VEHC_SEQ_EVENTS_DF.withColumn("id", monotonically_increasing_id())
//
//    personDF = personDF.join(VEHC_SEQ_EVENTS_DF,usingColumn = "id")
//    personDF.show()
//    personDF.printSchema()
//    personDF.select("DRIVER_AGE","AGE").show()
//    filterAge(personDF,46).select("CRASH_NUMB","DRIVER_AGE").show()
    personDF = personDF.withColumn("FATALITY_BIN", when(col("NUMB_FATAL_INJR") === 0, 0).otherwise(1))
    val personDFSubset = personDF.select(

      "LAT"
      ,"LON"
      ,"CRASH_DATETIME"
      ,"DISTRICT_NUM"
      ,"LCLTY_NAME"
      ,"OWNER_ADDR_CITY_TOWN"
      ,"OWNER_ADDR_STATE"
      ,"VEHC_REG_STATE"
      ,"WEATH_COND_DESCR"
      ,"ROAD_SURF_COND_DESCR"
//      ,"MAX_INJR_SVRTY_CL"
      ,"MANR_COLL_DESCR"
      ,"FIRST_HRMF_EVENT_DESCR"
//      ,"MOST_HRMFL_EVT_CL"
      ,"VEHC_CONFG_DESCR"
//      ,"HIT_RUN_DESCR"
      ,"AGE_DRVR_YNGST"
      ,"AGE_DRVR_OLDEST"
//      ,"DRVR_DISTRACTED_CL"
      ,"DRVR_CNTRB_CIRC_CL"
      ,"DRIVER_AGE"
      ,"DRIVER_DISTRACTED_TYPE_DESCR"
      ,"DRVR_LCN_STATE"
      ,"DRUG_SUSPD_TYPE_DESCR"
//      ,"SFTY_EQUP_DESC_1"
//      ,"SFTY_EQUP_DESC_2"
      ,"ALC_SUSPD_TYPE_DESCR"
      ,"FATALITY_BIN"
    )
    println("personDF_split_cols")
//    val personDF_split_cols = split_columns(personDF,"VEHC_SEQ_EVENTS")
//    personDF_split_cols
//      .select("CRASH_NUMB","Vehicle_One","Vehicle_Config")
//      .show()
//    personDF_split_cols.printSchema()
//    personDF_split_cols.rdd
//      .repartition(1)
//      .map(_.toString())
//      .saveAsTextFile("target/personDF_split_cols")

//    personDFSubset.repartition(1).write.csv("target/personDFSubset")
//    val cleanDF = cleanData(personDFSubset)
//    println("cleanDF: ",cleanDF.printSchema())
//    cleanDF.rdd
//      .repartition(1)
//      .map(_.toString()
//        .replace("[","")
//        .replace("]", "")
//        .replace(" (","")
//        .replace(")",""))
//      .saveAsTextFile("target/cleandf/data")

//    predictFatalityLR(cleanDF)
    val personDFSubset_dt = split_date_time(personDFSubset,"CRASH_DATETIME")
    val personDFSubset_dt_drvr = split_columns_driver(personDFSubset_dt,"DRVR_CNTRB_CIRC_CL")
    val NBTrainTestInput = personDFSubset_dt_drvr.drop("LAT","LON","CRASH_DATETIME","DRVR_CNTRB_CIRC_CL")

    NBTrainTestInput.printSchema()
    println("NB Input: ",NBTrainTestInput.show())
    ////////////////////////////////////////////////////////
    val df1 = NBTrainTestInput.na.fill("NA")
    df1.show()
    val indexer = new StringIndexer()
      .setInputCols(Array(
        "DISTRICT_NUM"
        ,"LCLTY_NAME"
        ,"OWNER_ADDR_CITY_TOWN"
        , "OWNER_ADDR_STATE"
        , "VEHC_REG_STATE"
        , "WEATH_COND_DESCR"
        , "ROAD_SURF_COND_DESCR"
        , "FIRST_HRMF_EVENT_DESCR"
        , "VEHC_CONFG_DESCR"
        , "AGE_DRVR_YNGST"
        , "AGE_DRVR_OLDEST"
        , "DRIVER_DISTRACTED_TYPE_DESCR"
        , "DRVR_LCN_STATE"
        , "DRUG_SUSPD_TYPE_DESCR"
        , "ALC_SUSPD_TYPE_DESCR"
        ,"First_DRVR_CNTRB_CIRC_CL"
        ,"CRASH_MONTH"
        ,"CRASH_DAY"
        ,"CRASH_YEAR"
        ,"CRASH_HOUR"
      ))
      .setOutputCols(Array(
        "DISTRICT_NUM_index"
        ,"LCLTY_NAME_index"
        ,"OWNER_ADDR_CITY_TOWN_index"
        , "OWNER_ADDR_STATE_index"
        , "VEHC_REG_STATE_index"
        , "WEATH_COND_DESCR_index"
        , "ROAD_SURF_COND_DESCR_index"
        , "FIRST_HRMF_EVENT_DESCR_index"
        , "VEHC_CONFG_DESCR_index"
        , "AGE_DRVR_YNGST_index"
        , "AGE_DRVR_OLDEST_index"
        , "DRIVER_DISTRACTED_TYPE_DESCR_index"
        , "DRVR_LCN_STATE_index"
        , "DRUG_SUSPD_TYPE_DESCR_index"
        , "ALC_SUSPD_TYPE_DESCR_index"
        ,"First_DRVR_CNTRB_CIRC_CL_index"
        ,"CRASH_MONTH_index"
        ,"CRASH_DAY_index"
        ,"CRASH_YEAR_index"
        ,"CRASH_HOUR_index"
      ))
      .setHandleInvalid("keep")

    val indexed = indexer.fit(df1).transform(df1)
    indexed.show()


    val assembler = new VectorAssembler()
      .setInputCols(Array(
        "DISTRICT_NUM_index"
        ,"LCLTY_NAME_index"
        ,"OWNER_ADDR_CITY_TOWN_index"
        , "OWNER_ADDR_STATE_index"
        , "VEHC_REG_STATE_index"
        , "WEATH_COND_DESCR_index"
        , "ROAD_SURF_COND_DESCR_index"
        , "FIRST_HRMF_EVENT_DESCR_index"
        , "VEHC_CONFG_DESCR_index"
        , "AGE_DRVR_YNGST_index"
        , "AGE_DRVR_OLDEST_index"
        , "DRIVER_DISTRACTED_TYPE_DESCR_index"
        , "DRVR_LCN_STATE_index"
        , "DRUG_SUSPD_TYPE_DESCR_index"
        , "ALC_SUSPD_TYPE_DESCR_index"
        ,"First_DRVR_CNTRB_CIRC_CL_index"
        ,"CRASH_MONTH_index"
        ,"CRASH_DAY_index"
        ,"CRASH_YEAR_index"
        ,"CRASH_HOUR_index"))
      .setOutputCol("features")
      .setHandleInvalid("skip")
    println("assembler: ", assembler)
    val df3 = assembler.transform(indexed).select(col("FATALITY_BIN").cast(DoubleType).as("label"), col("features"))
    println(assembler.params)
    val labeledNBTestTrainInput = df3.rdd.map(row => LabeledPoint(
      row.getAs[Double]("label"),
      org.apache.spark.mllib.linalg.Vectors.fromML(row.getAs[org.apache.spark.ml.linalg.SparseVector]("features"))
    ))
    ////////////////////////////////////
    println("running Naive Bayes model")
    val labeled = df3.rdd.map(row => LabeledPoint(
      row.getAs[Double]("label"),
      org.apache.spark.mllib.linalg.Vectors.fromML(row.getAs[org.apache.spark.ml.linalg.SparseVector]("features"))
    ))

    ////////////////////////////////////////////////////////
    val predict_fatality_NB_model = naiveBayesModel(labeled)
//    predictProbabilityOfCrash()
    println("predict_fatality_NB_model labels: ",predict_fatality_NB_model.labels)
    println("predict_fatality_NB_model modelType: ",predict_fatality_NB_model.modelType)
    val test_data = List((
      "6"//"DISTRICT_NUM"
      ,"DORCHESTER" //,"LCLTY_NAME"
      ,"NA"//,"OWNER_ADDR_CITY_TOWN"
      ,"NA"//, "OWNER_ADDR_STATE"
      ,"NA"//, "VEHC_REG_STATE"
      ,"Clear"//, "WEATH_COND_DESCR"
      ,"Dry"//, "ROAD_SURF_COND_DESCR"
      ,"NA"//, "FIRST_HRMF_EVENT_DESCR"
      ,"NA"//, "VEHC_CONFG_DESCR"
      ,"NA"//, "AGE_DRVR_YNGST"
      ,"NA"//, "AGE_DRVR_OLDEST"
      ,"NA"//, "DRIVER_DISTRACTED_TYPE_DESCR"
      ,"NA"//, "DRVR_LCN_STATE"
      ,"NA"//, "DRUG_SUSPD_TYPE_DESCR"
      ,"NA"//, "ALC_SUSPD_TYPE_DESCR"
      ,"Inattention"//,"First_DRVR_CNTRB_CIRC_CL"
      ,"12"//,"CRASH_MONTH"
      ,"10"//,"CRASH_DAY"
      ,"20"//,"CRASH_YEAR"
      ,"11"//,"CRASH_HOUR"

    ))
      .toDF(
      "DISTRICT_NUM"
      ,"LCLTY_NAME"
      ,"OWNER_ADDR_CITY_TOWN"
      , "OWNER_ADDR_STATE"
      , "VEHC_REG_STATE"
      , "WEATH_COND_DESCR"
      , "ROAD_SURF_COND_DESCR"
      , "FIRST_HRMF_EVENT_DESCR"
      , "VEHC_CONFG_DESCR"
      , "AGE_DRVR_YNGST"
      , "AGE_DRVR_OLDEST"
      , "DRIVER_DISTRACTED_TYPE_DESCR"
      , "DRVR_LCN_STATE"
      , "DRUG_SUSPD_TYPE_DESCR"
      , "ALC_SUSPD_TYPE_DESCR"
      , "First_DRVR_CNTRB_CIRC_CL"
      , "CRASH_MONTH"
      , "CRASH_DAY"
      , "CRASH_YEAR"
      , "CRASH_HOUR"
    )
    test_data.show()


    val indexed_test_data = indexer.fit(df1).transform(test_data)
    println("indexed_test_data: ",indexed_test_data)
    indexed_test_data.show()
    println("indexed_test_data count: ",indexed_test_data.count())
    val assembled_test_data = assembler.transform(indexed_test_data).select(col("features"))
    println("assembled_test_data count: ",assembled_test_data.count())
    println("assembled_test_data: ",assembled_test_data.take(10).foreach(x => println(x + " ")))
    assembled_test_data.show()
    val assembled_test_data_vec = assembled_test_data.select(array(assembled_test_data.columns.map(col(_)): _*)).rdd.map(_.getSeq[Double](0))
    assembled_test_data_vec.take(10).foreach(x => println(x + " "))
//    val assembled_test_data_vector = assembled_test_data.map{x:Row => x.getAs[Vector](0)}
    import org.apache.spark.mllib.linalg.Vectors

    val assembled_test_data_vector = assembled_test_data
      .rdd
      .map{
        row => Vectors.dense(row.getAs[Seq[Double]]("features").toArray)
      }
//    assembled_test_data_vector.collect().foreach(println)
    val input_prediction = predict_fatality_NB_model.predict(assembled_test_data_vector)
    val input_prediction_prob = predict_fatality_NB_model.predictProbabilities(assembled_test_data_vector)
    println("predictions completed!")
    input_prediction.toDF().show()
//    input_prediction.take(10).foreach(x => println(x + " "))
//    input_prediction.collect().foreach(println)
//    println("input_prediction: ",input_prediction.collect().foreach(println))

//    println("input_prediction_prob: ",input_prediction_prob.collect().foreach(println))

  }


  def naiveBayesModel(labeled:RDD[LabeledPoint]) :NaiveBayesModel = {

    // Split data into training (60%) and test (40%).
    val Array(training, test) = labeled.randomSplit(Array(0.66, 0.34))
    print("sample test data ")
    test.take(10).foreach(x => println(x + " "))

    val model = NaiveBayes.train(training, lambda = 1.0, modelType = "multinomial")

    val predictionAndLabel = test.map(p => (model.predict(p.features), p.label))
    println("prediction and label")
    predictionAndLabel.take(10).foreach(x => println(x + " "))
    val probAndLabel = test.map(p => (model.predictProbabilities(p.features), p.label))
    println("probability and label")
    probAndLabel
      .filter {case (_, v) => v == 1.0}
      .take(10)
      .foreach(x => println(x + " "))
    val accuracy = 1.0 * predictionAndLabel.filter(x => x._1 == x._2).count() / test.count()
    println("accuracy of Naive Bayes: ", accuracy)

//    // Save and load model
//    model.save(sc, "target/tmp/myNaiveBayesModel")
//    val sameModel = NaiveBayesModel.load(sc, "target/tmp/myNaiveBayesModel")
    // $example off$
    model
  }

  def split_columns(df: sql.DataFrame, column_name: String) :sql.DataFrame = {
    var dataframe = df.na.fill("V1:()", Array(column_name))
    dataframe = dataframe.withColumn("Vehicle_One", split(col(column_name),"\\)").getItem(0))
    dataframe = dataframe.withColumn("Vehicle_Config",regexp_replace(col("Vehicle_One"),"V1:\\(",""))
    dataframe.show()
    dataframe
  }
  def split_date_time(df: sql.DataFrame, column_name: String) :sql.DataFrame = {
    var dataframe = df.withColumn("CRASH_MONTH", split(col(column_name),"[\\/]").getItem(0))
      .withColumn("CRASH_DAY", split(col(column_name),"[\\/]").getItem(1))
      .withColumn("CRASH_YEAR_TIME", split(col(column_name),"[\\/]").getItem(2))
    dataframe = dataframe.withColumn("CRASH_YEAR", split(col("CRASH_YEAR_TIME"),"\\ ").getItem(0))
    dataframe = dataframe.withColumn("TIME", split(col("CRASH_YEAR_TIME"),"\\ ").getItem(1)).drop("CRASH_YEAR_TIME")
    dataframe = dataframe.withColumn("CRASH_HOUR", split(col("TIME"),"\\:").getItem(0)).drop("TIME")
    dataframe.show()
    dataframe
  }

  def split_columns_driver(df: sql.DataFrame, column_name: String) :sql.DataFrame = {
    var dataframe = df.na.fill("D1:()", Array(column_name))
    dataframe = dataframe.withColumn("Driver_One", split(col(column_name),"[\\)]*[\\/]").getItem(0))
    dataframe = dataframe.withColumn("Driver_Control",regexp_replace(col("Driver_One"),"[D1:]*[D2:]*[D3:]*[D4:]","")).drop("Driver_One")
    dataframe = dataframe.withColumn("First",regexp_replace(col("Driver_Control"),"[\\(]*[\\)]","")).drop("Driver_Control")
    dataframe = dataframe.withColumn("First_" + column_name,regexp_replace(col("First"),"[\\(]","")).drop("First")
    //dataframe.coalesce(1).write.option("header", "true").csv("src/splitDriver.csv")
    dataframe.show()
    dataframe
  }

//  def predictProbabilityOfCrash(df: sql.DataFrame) //:RDD[(Vector,Double)]
//  = {
//    var input = scala.io.StdIn.readLine()
//    println(input)
//  }




}


