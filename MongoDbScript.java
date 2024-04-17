class MongoDbScript{
	public static void main(String arg[]){
		
		 String sampleDateStr = "2024-04-16 16:07:00";

                    String serviceCollection="student";
                 	mongoDbScriptWriting(sampleDateStr,serviceCollection);
		String newCollection="test";
               	   String path="C:\\Users\\jayar\\OneDrive\\Desktop\\output.txt";
                	insertFromFile(path,newCollection);
		
	}
	
	public static void mongoDbScriptWriting(String sampleDateStr,String serviceCollection) {
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {

    	 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         Date timestamp = dateFormat.parse(sampleDateStr);
            MongoDatabase database = mongoClient.getDatabase("mydb");
            MongoCollection<Document> collection = database.getCollection(serviceCollection);

            for (Document document : collection.find()) {
                ObjectId objectId = document.getObjectId("_id");
                Date documentDate = objectId.getDate();
                
                if (documentDate.after(timestamp)) {
                    String json = document.toJson();
                    appendToTxtFile(json);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private static void appendToTxtFile(String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\insertScript.txt", true))) {
            
        	
        	writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    private static void insertFromFile(String filePath,String collectionName) {
    	
    	try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            // Connect to database
            MongoDatabase database = mongoClient.getDatabase("mydb");

            // Get reference to collection
            MongoCollection<Document> collection = database.getCollection(collectionName);

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                  
                	Document document = Document.parse(line);
                    collection.insertOne(document);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	
	
}