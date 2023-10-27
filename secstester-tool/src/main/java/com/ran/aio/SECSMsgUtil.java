package com.ran.aio;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SECSMsgUtil {

	private static volatile SECSMsgUtil instance = null;
	private static Charset charset = StandardCharsets.US_ASCII;

	private SECSMsgUtil() {
	}

	public static SECSMsgUtil getInstance() {
		if (instance == null) {
			synchronized (SECSMsgUtil.class) {
				if (instance == null) {
					instance = new SECSMsgUtil();
				}
			}
		}
		return instance;
	}

	public SECSBody buildSecsBody(byte[] arr) {
		SECSBody body = null;

		boolean readFormat = true;
		boolean readLenByte = false;
		boolean readValue = false;
		int lenByteNum = 0;
		SECSFormat tempFormat = null;
		SECSItem itemArr[] = null;

		List<Byte> tempValueArr = null;

		try {
			body = new SECSBody();
			for (int i = 0; i < arr.length; i++) {
				if (readFormat) {

					String tempStr = getBinaryStrFromByte(arr[i]);
					int tempInt = Integer.parseInt(tempStr.substring(0, 5), 2);
					tempFormat = SECSFormat.get(tempInt);
					lenByteNum = Integer.parseInt(tempStr.substring(6, 8), 2);

					readFormat = false;
					readLenByte = true;
				} else if (readLenByte) {

					lenByteNum = Byte.toUnsignedInt(arr[i]);

					if (tempFormat.equals(SECSFormat.L)) {

						itemArr = new SECSItem[lenByteNum];
						readFormat = true;
						readLenByte = false;

					} else {
						// tempValueArr = new byte[lenByteNum];
						tempValueArr = new ArrayList<>();

						readLenByte = false;
						readValue = true;
					}
				} else if (readValue) {
					tempValueArr.add(arr[i]);
					lenByteNum--;
					if (lenByteNum == 0) {

						// if(itemArr!=null && itemArr.l )

						readValue = false;
						readFormat = true;
						tempValueArr.clear();
					}

				}

			}

		} catch (Exception e) {

		}

		return body;
	}

	public byte[] pareSecsBodyToArr(SECSBody secsBody, int len) {
		byte arr[] = new byte[len-10];
		Queue<String> root = secsBody.getOriginQueue();
		int index = 0;
		while (!root.isEmpty()) {
			String tmp = root.poll();

			// [i]=

			String tempArr[] = tmp.split(":");

			if ("List".equals(tempArr[0])) {
				// 1.parse format
				String format = getBinaryStrFromByte(Byte.parseByte(0 + ""));
				// 2.parse len
				while (format.length() < 6) {

					format = "0" + format;
				}

				String targetBinaryStr = format + "01";
				arr[index] = bit2byte(targetBinaryStr);
				index++;

				String itemLen = getBinaryStrFromByte(Byte.parseByte(tempArr[1]));
				while (itemLen.length() < 8) {

					itemLen = "0" + itemLen;
				}
				arr[index] = bit2byte(itemLen);
				index++;

			} else {

				//prepare.A[4] U4[4] ,at first,remove the length.
				String tempFormat = tempArr[0].replaceAll("\\[(.+?)\\]", "");
				SECSFormat format = SECSFormat.valueOf(tempFormat);
				// 1.parse format

				switch (format) {
				case A: {
					int blockLen = 0;
					int itemLen = tempArr[1].length();
					// 2.parse content
					String lenBinaryString = Integer.toBinaryString(itemLen);

					blockLen = lenBinaryString.length() / 8;
					blockLen = blockLen + lenBinaryString.length() % 8 == 0 ? 0 : 1;
					// 3.parse len from content len
					String formatLenBinary = Integer.toBinaryString(blockLen);
					if(formatLenBinary.length() < 2) {
						formatLenBinary = "0" + formatLenBinary;
					}
					
					//A. format + len Byte
					arr[index] = bit2byte("010000" + formatLenBinary);
					index++;

					

					String tempBinaryArr[] = new String[blockLen];
					for (int i = 0; i < blockLen; i++) {

						if (i == blockLen - 1) {
							int start = 0;
							int end = 0;
							if (lenBinaryString.length() <= 8) {
							end = lenBinaryString.length();
							}else {
							end = lenBinaryString.length() - blockLen * 8;
							}
							String binaryTmp = lenBinaryString.substring(start, end);
							tempBinaryArr[i] = binaryTmp;
						} else {
							int start = lenBinaryString.length() - 8 - (i * 8);
							int end = lenBinaryString.length() - (i * 8);
							String binaryTmp = lenBinaryString.substring(start, end);
							tempBinaryArr[i] = binaryTmp;
						}

					}

					//B.len byte
					for (int i = blockLen - 1; i >= 0; i--) {
						arr[index] = bit2byte(tempBinaryArr[i]);
						index++;
					}

					//byte formatByte[] = int2Byte(format.getCode());
					//StringBuffer tempBuffer = new StringBuffer();
					//C.body byte
					for (int i = 0; i < itemLen; i++) {
						//tempBuffer.append(getBinaryStrFromByte(formatByte[i]));
						arr[index] = (byte)(tempArr[1].charAt(i));
						index++;
					}
					break;
				}
				case U4: {

					byte formatByte[] = int2Byte(format.getCode());
					int itemLen = 4;
					arr[index] = bit2byte("10110001");
					index++;

					arr[index] = bit2byte("0100");
					index++;

					byte[] byteArr = int2Byte(Integer.parseInt(tempArr[1]));
					for (int i = byteArr.length -1 ; i >=0 ; i--) {

						arr[index] = byteArr[i];
						index++;
					}

					break;
				}
				case BINARY:{
					arr[index] = bit2byte("100001");
					index++;
					
					arr[index] = bit2byte("1");
					index++;
					
					arr[index] = Byte.parseByte(tempArr[1]);
					index++;
					break;
				}
				case BOOL:{
					arr[index] = bit2byte("100101");
					index++;
					
					arr[index] = bit2byte("1");
					index++;
					
					//00 false
					//FF true
					String localTmp = tempArr[1].equals("False")?"00":"FF";
					
					//arr[index] = HexToByte(tempArr[1]);
					arr[index] = HexToByte(localTmp);
					index++;
					break;
				}
				
				case U1:{
					byte formatByte[] = int2Byte(format.getCode());
					int itemLen = 4;
					arr[index] = bit2byte("10100101");
					index++;

					arr[index] = bit2byte("1");
					index++;
					
					int tempInt = Integer.parseInt(tempArr[2]);
					
					
					arr[index] =  (byte) (Integer.parseInt(tempArr[1]) & 0xff);
					index++;
					break;
					
				}
				default: {

				}
					break;
				}

			}

		}

		return arr;

	}

	public Byte[] parseSecsBody(Queue<String> root) {
		// TODO Auto-generated method stub
		//byte arr[] = new byte[len-10];
		List<Byte> byteList = new ArrayList<Byte>(); 
		int index = 0;
		while (!root.isEmpty()) {
			String tmp = root.poll();

			// [i]=

			String tempArr[] = tmp.split(":");

			if ("List".equals(tempArr[0])) {
				// 1.parse format
				String format = getBinaryStrFromByte(Byte.parseByte(0 + ""));
				// 2.parse len
				while (format.length() < 6) {

					format = "0" + format;
				}

				String targetBinaryStr = format + "01";
				//arr[index] = bit2byte(targetBinaryStr);
				byteList.add(bit2byte(targetBinaryStr));
				index++;

				String itemLen = getBinaryStrFromByte(Byte.parseByte(tempArr[1]));
				while (itemLen.length() < 8) {

					itemLen = "0" + itemLen;
				}
				//arr[index] = bit2byte(itemLen);
				byteList.add(bit2byte(itemLen));
				index++;

			} else {

				//prepare.A[4] U4[4] ,at first,remove the length.
				String tempFormat = tempArr[0].replaceAll("\\[(.+?)\\]", "");
				SECSFormat format = SECSFormat.valueOf(tempFormat);
				// 1.parse format

				switch (format) {
				case A: {
					int blockLen = 0;
					int itemLen = tempArr[1].length();
					// 2.parse content
					String lenBinaryString = Integer.toBinaryString(itemLen);

					blockLen = lenBinaryString.length() / 8;
					blockLen = blockLen + lenBinaryString.length() % 8 == 0 ? 0 : 1;
					// 3.parse len from content len
					String formatLenBinary = Integer.toBinaryString(blockLen);
					if(formatLenBinary.length() < 2) {
						formatLenBinary = "0" + formatLenBinary;
					}
					
					//A. format + len Byte
					//arr[index] = bit2byte("010000" + formatLenBinary);
					byteList.add(bit2byte("010000" + formatLenBinary));
					index++;

					

					String tempBinaryArr[] = new String[blockLen];
					for (int i = 0; i < blockLen; i++) {

						if (i == blockLen - 1) {
							int start = 0;
							int end = 0;
							if (lenBinaryString.length() <= 8) {
							end = lenBinaryString.length();
							}else {
							end = lenBinaryString.length() - blockLen * 8;
							}
							String binaryTmp = lenBinaryString.substring(start, end);
							tempBinaryArr[i] = binaryTmp;
						} else {
							int start = lenBinaryString.length() - 8 - (i * 8);
							int end = lenBinaryString.length() - (i * 8);
							String binaryTmp = lenBinaryString.substring(start, end);
							tempBinaryArr[i] = binaryTmp;
						}

					}

					//B.len byte
					for (int i = blockLen - 1; i >= 0; i--) {
						//arr[index] = bit2byte(tempBinaryArr[i]);
						byteList.add( bit2byte(tempBinaryArr[i]));
						index++;
					}

					//byte formatByte[] = int2Byte(format.getCode());
					//StringBuffer tempBuffer = new StringBuffer();
					//C.body byte
					for (int i = 0; i < itemLen; i++) {
						//tempBuffer.append(getBinaryStrFromByte(formatByte[i]));
						//arr[index] = (byte)(tempArr[1].charAt(i));
						byteList.add((byte)(tempArr[1].charAt(i)));
						index++;
					}
					break;
				}
				case U4: {

					byte formatByte[] = int2Byte(format.getCode());
					int itemLen = 4;
					//arr[index] = bit2byte("10110001");
					byteList.add(bit2byte("10110001"));
					index++;

					//arr[index] = bit2byte("0100");
					byteList.add(bit2byte("0100"));
					index++;

					byte[] byteArr = int2Byte(Integer.parseInt(tempArr[1]));
					for (int i = byteArr.length -1 ; i >=0 ; i--) {

						//arr[index] = byteArr[i];
						byteList.add(byteArr[i]);
						index++;
					}

					break;
				}
				case BINARY:{
					//arr[index] = bit2byte("100001");
					byteList.add(bit2byte("100001"));
					index++;
					
					//arr[index] = bit2byte("1");
					byteList.add(bit2byte("1"));
					index++;
					
					//arr[index] = Byte.parseByte(tempArr[1]);
					byteList.add(Byte.parseByte(tempArr[1]));
					index++;
					break;
				}
				case BOOL:{
					//arr[index] = bit2byte("100101");
					byteList.add(bit2byte("100101"));
					index++;
					
					//arr[index] = bit2byte("1");
					byteList.add(bit2byte("1"));
					index++;
					
					//00 false
					//FF true
					String localTmp = tempArr[1].equals("False")?"00":"FF";
					
					//arr[index] = HexToByte(tempArr[1]);
					//[index] = HexToByte(localTmp);
					byteList.add(HexToByte(localTmp));
					index++;
					break;
				}
				
				case U1:{
					byte formatByte[] = int2Byte(format.getCode());
					int itemLen = 4;
					//arr[index] = bit2byte("10100101");
					byteList.add(bit2byte("10100101"));
					index++;

					//arr[index] = bit2byte("1");
					byteList.add(bit2byte("1"));
					index++;
					
					//int tempInt = Integer.parseInt(tempArr[2]);
					
					
					//arr[index] =  (byte) (Integer.parseInt(tempArr[1]) & 0xff);
					byteList.add((byte) (Integer.parseInt(tempArr[1]) & 0xff));
					index++;
					break;
					
				}
				default: {

				}
					break;
				}

			}

		}

		return byteList.toArray(new Byte[0]);
	}

	
	public SECSMsg buildSecsMsgFromStr(String msg) {
		
		
		
		
		
		return null;
		
	}
	
	
	
	public Queue<String> parseStrMsgToQueue(String msg){
		Queue<String> queue = new LinkedBlockingQueue<String>();
		String arr []  = msg.split(System.getProperty("line.separator"));
		if(arr.length==1) {
			arr = msg.split("\n");
		}
		if(arr.length==2) {
			return queue;
		}else {
		
		
		//String listReg = "\\<L\\[(\\d+)\\]";
		//(?<=\<\L\[)(.+?)(?=\])
		String listReg = "(?<=\\<L\\[)(.+?)(?=\\])";
		String dataReg = "\\S+\\[(\\d+)\\]:\\S+";
		Pattern listRegPattern = Pattern.compile(listReg);
		Pattern dataRegPattern = Pattern.compile(dataReg);
		boolean findFlag =false;
		//len 0 timestamp
		//len 1 secs header

		for(int i=2; i<arr.length ; i++){
			String tmp = arr[i];
			//List ITEM
			Matcher listMatcher = listRegPattern.matcher(tmp);
			findFlag = listMatcher.find();
			if(findFlag) {
				//String listSize = tmp.replace("-", "").replace(listReg, "");
				String listSize =listMatcher.group(); 
				/*
				if(listSize.indexOf("-")>0) {
					listSize = tmp.replace("-", "");
					
				}
				*/
				queue.add("List:"+listSize);
				
			}
			else{//DATA ITEM
				Matcher dataMatcher = dataRegPattern.matcher(tmp);
				findFlag = dataMatcher.find();
				if(findFlag) {
					String dataItemArr[] = tmp.split(":");
					//String dataItem = dataItemArr[0].replaceAll("-","");
					String dataItem = dataItemArr[0].replaceAll("\\[(\\d+)\\]","").replaceAll("-","");
					try {
					SECSFormat format = SECSFormat.valueOf(dataItem);
					}catch (IllegalArgumentException e) {
						System.out.println("Can't find SECS item format in len "+i+".Please check SECS message.");
						return null;
					}
					queue.add(dataItem+":"+tmp.replaceAll("\\S+\\[(\\d+)\\]:", ""));
					
					
				}
				/*
				else {
					System.out.println("Can't find SECS item in len "+i+".Please check SECS message.");
					return null;
				}*/
				
				
			}
			
			
		}}
		
		return queue;
		
	}
	
	
	
	public byte[] parseHeader(SECSHeader header) {
		
		byte arr[] = new byte[10];
		
		//LSB
		//0 sessionId
		int sessionId = header.getSessionId();
		arr[0] =(byte) (sessionId & 0xff);
		//1 deviceId
		
		int deviceId = header.getDeviceId();
		arr[1] =(byte) (deviceId & 0xff);
		//2 headerByte2
		int headerByte2 =header.getStreamNo();
		if(header.isNeedReply()) {
			String wBitStreamStr = getBinaryStrFromByte((byte) (headerByte2 & 0xff));

			char charArr[] = wBitStreamStr.toCharArray();

			charArr[0] = '1';

			//header.setStreamNo(Byte.toUnsignedInt(bit2byte(new String(charArr))));
			arr[2] = bit2byte(new String(charArr));
		}else {
		arr[2] =(byte) (headerByte2 & 0xff);
		}
		
		
		
		//3 headerByte3
		int headerByte3=header.getFunctionNo();
		arr[3] =(byte) (headerByte3 & 0xff);
		//4 PType
		int pType =0;
		arr[4] =(byte) (0);
		//5 SType
		int sType = header.getsType().getCode();
		arr[5] =(byte) (sType & 0xff);
		//6-9 SystemByte
		
		//int systemByte = Integer.decode(header.getSystemByte());
		
		int systemByte = new BigInteger(header.getSystemByte(), 16).intValue();
		arr[9] =(byte) (systemByte & 0xff);
		arr[8] =(byte) (systemByte >> 8);
		arr[7] =(byte) (systemByte >> 16);
		arr[6] =(byte) (systemByte >> 24);
		
		return arr;
		
	}
	
	public SECSBody buildSecsBodyForSTR(byte[] arr) {
		SECSBody body = null;

		boolean readFormat = true;
		boolean readLenByte = false;
		boolean readValue = false;
		int lenByteNum = 0;
		int bodyByteNum = 0;
		int copyOfByteNum = 0;
		
		SECSFormat tempFormat = null;
		StringBuilder sb = new StringBuilder();
		SECSItem itemArr[] = null;
		Queue<String> queue = new ConcurrentLinkedDeque<String>();
		List<Byte> tempValueArr = null;
		List<Byte> tempLenArr = null;
		try {
			body = new SECSBody();
			for (int i = 0; i < arr.length; i++) {
				if (readFormat) {

					String tempStr = getBinaryStrFromByte(arr[i]);
					String formatStr = tempStr.substring(0, 6);
					String lenStr = tempStr.substring(6, 8);
					int tempInt = Integer.parseInt(formatStr, 2);
					tempFormat = SECSFormat.get(tempInt);
					lenByteNum = Integer.parseInt(lenStr, 2);

					if (!tempFormat.equals(SECSFormat.L)) {
						tempLenArr = new ArrayList<>();
					}

					readFormat = false;
					readLenByte = true;
				} else if (readLenByte) {

					// lenByteNum = Byte.toUnsignedInt(arr[i]);

					if (tempFormat.equals(SECSFormat.L)) {

						// itemArr = new SECSItem[lenByteNum];
						lenByteNum = Byte.toUnsignedInt(arr[i]);
						sb.append("List [" + lenByteNum + "]").append(System.lineSeparator());
						queue.add("List:" + lenByteNum);
						readFormat = true;
						readLenByte = false;

					} else {
						tempLenArr.add(arr[i]);
						lenByteNum--;
						if (lenByteNum == 0) {

							String tempVal = getStrValFromByte(SECSFormat.U4, tempLenArr.stream().toArray(Byte[]::new));
							// tempValueArr = new byte[lenByteNum];
							bodyByteNum = Integer.parseInt(tempVal);
							copyOfByteNum = bodyByteNum;
							tempValueArr = new ArrayList<>();
							
							readLenByte = false;
							readValue = true;
							tempLenArr.clear();
						}
					}
				} else if (readValue) {
					tempValueArr.add(arr[i]);
					bodyByteNum--;
					if (bodyByteNum == 0) {

						// if(itemArr!=null && itemArr.l )
						String tempVal = getStrValFromByte(tempFormat, tempValueArr.stream().toArray(Byte[]::new));
						sb.append(tempFormat.toString()).append(":").append(tempVal).append(System.lineSeparator());
						//queue.add(tempFormat.toString()+" ["+copyOfByteNum +"]" + (":") + (tempVal));
						//queue.add(tempFormat.toString()+(":")+copyOfByteNum  + (":") + (tempVal));
						queue.add(tempFormat.toString()+("[")+copyOfByteNum  + ("]:") + (tempVal));
						tempValueArr.toArray(new Byte[0]);

						readValue = false;
						readFormat = true;
						tempValueArr.clear();
					}

				}

			}
		
			// System.out.println(sb.toString());

			body = new SECSBody();

			if(queue.size()==0) {
				
			}else {
			Queue<String> copyQueue = new LinkedBlockingQueue<String>(queue);
			body.setOriginQueue(copyQueue);

			LinkSecsItem secsBody = buildSecs(queue);
			//secsBody.ite(secsBody, 0);
			body.setRootItem(secsBody);
			}
			
		} catch (Exception e) {

		}

		return body;
	}

	public LinkSecsItem buildSecs(Queue<String> queue) {

		String tmp = queue.poll();

		String arr[] = tmp.split(":");

		if ("List".equals(arr[0])) {

			List<LinkSecsItem> ll = new ArrayList<LinkSecsItem>();
			if (Integer.parseInt(arr[1]) == 0) {

				// ll.add(buildSecs(null));
			} else {

				for (int i = 0; i < Integer.parseInt(arr[1]); i++) {
					ll.add(buildSecs(queue));
				}
			}

			return new LinkSecsItem(ll);
		} else {

			return new LinkSecsItem(tmp);

		}
	}

	public String getStrValFromByte(SECSFormat format, Byte[] ByteArr) {
		String temp = "";
		byte arr[] = toPrimitives(ByteArr);

		switch (format) {

		case A:
			temp = new String(arr, charset);
			break;
		case U4:
			BigInteger tempInt = new BigInteger(arr);
			temp = tempInt.toString();
			break;
		case U1:
			int val = arr[0] & 0xff;
			temp =val+"";
			break;


		default:
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < arr.length; i++)
				sb.append(bytes2HexString(arr[i]));
			temp = sb.toString();
			if(format.equals(SECSFormat.BOOL)) {
				temp = temp.equals("00")?"False":"True";
			}
			
			break;

		}

		return temp;
	}

	public SECSItem getSecsItemByByte(byte[] arr, int startPosition) {
		SECSItem item = null;

		// 1.FORMAT

		// 2.LENTH

		// 3.IF LIST

		// 4.ELSE GET VALUE.

		return item;

	}

	public SECSHeader buildSecsHeader(byte[] arr) {

		SECSHeader header = new SECSHeader();

		header.setBinArr(arr);
		
		header.setSessionId(Byte.toUnsignedInt(arr[0]));
		header.setDeviceId(Byte.toUnsignedInt(arr[1]));

		String wBitStreamStr = getBinaryStrFromByte(arr[2]);

		header.setNeedReply(wBitStreamStr.charAt(0) == '1' ? true : false);

		char charArr[] = wBitStreamStr.toCharArray();

		charArr[0] = '0';

		header.setStreamNo(Byte.toUnsignedInt(bit2byte(new String(charArr))));

		header.setFunctionNo(Byte.toUnsignedInt(arr[3]));

		header.setpType(null);

		header.setsType(SessionType.get(Byte.toUnsignedInt(arr[5])));

		StringBuilder sb = new StringBuilder();
		for (byte i : arr) {
			sb.append(bytes2HexString(i));
		}

		header.setOriginalStr(sb.toString());

		header.setSystemByte(sb.substring(12, 20));

		return header;
	}

	/**
	 * 鎶奲yte杞寲鎴�2杩涘埗瀛楃涓�
	 * 
	 * @param b
	 * @return
	 */
	private String getBinaryStrFromByte(byte b) {
		String result = "";
		byte a = b;
		;
		for (int i = 0; i < 8; i++) {
			byte c = a;
			a = (byte) (a >> 1);// 姣忕Щ涓�浣嶅鍚屽皢10杩涘埗鏁伴櫎浠�2骞跺幓鎺変綑鏁般��
			a = (byte) (a << 1);
			if (a == c) {
				result = "0" + result;
			} else {
				result = "1" + result;
			}
			a = (byte) (a >> 1);
		}
		return result;
	}

	/*
	 * 浜岃繘鍒惰浆byte
	 */
	private byte bit2byte(String bString) {
		byte result = 0;
		for (int i = bString.length() - 1, j = 0; i >= 0; i--, j++) {
			result += (Byte.parseByte(bString.charAt(i) + "") * Math.pow(2, j));
		}
		return result;
	}

	public byte[] int2Byte(int res) {
		byte arr[] = new byte[4];
		arr[0] = (byte) (res & 0xff);
		arr[1] = (byte) (res >> 8);
		arr[2] = (byte) (res >> 16);
		arr[3] = (byte) (res >>> 24);

		return arr;

	}
	
	public byte[] int2ByteReverse(int res) {
		byte arr[] = new byte[4];
		arr[3] = (byte) (res & 0xff);
		arr[2] = (byte) (res >> 8);
		arr[1] = (byte) (res >> 16);
		arr[0] = (byte) (res >>> 24);

		return arr;

	}

	public static int byte2Int(byte res[]) {
		int index = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) | ((res[2] << 24) >>> 8) | (res[3] << 24);
		return index;

	}

	public String bytes2HexString(byte b) {
		String ret = "";

		String hex = Integer.toHexString(b & 0xFF);
		if (hex.length() == 1) {
			hex = '0' + hex;
		}
		ret += hex.toUpperCase();

		return ret;
	}

	public Byte[] toObjects(byte[] bytesPrim) {
		Byte[] bytes = new Byte[bytesPrim.length];
		int i = 0;
		for (byte b : bytesPrim)
			bytes[i++] = b; // Autoboxing
		return bytes;
	}

	public byte[] toPrimitives(Byte[] oBytes) {
		byte[] bytes = new byte[oBytes.length];
		for (int i = 0; i < oBytes.length; i++) {
			bytes[i] = oBytes[i];
		}
		return bytes;
	}
	
	private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5',
	     '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
	

	
	/**
	 * Only for Str 2 lenght.
	 * @param tmp
	 * @return
	 */
	private byte HexToByte(String tmp) {
		return (byte) (charToByte(tmp.charAt(0)) << 4 | charToByte(tmp.charAt(1)));
	}
	
	/**
    * 将字符转换成字节
    *
    * @param c 字符
	    * @return byte 字节
	    */
	    private  byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
	     }
	
	
	
	

}
