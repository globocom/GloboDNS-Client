package com.globo.dnsapi.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.globo.dnsapi.model.Domain;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ObjectParser;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;
import com.thoughtworks.xstream.mapper.MapperWrapper;

public class App {

	public static void main(String[] args) throws Exception {
		final ObjectParser parser = new DnsApiCustomParser();
		
//		builder.setWrapperKeys(Arrays.asList(new String[] {"domain", "record"}));
		HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
		HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
			@Override
			public void initialize(HttpRequest request) throws IOException {
				request.setParser(parser);
			}
		});
		HttpHeaders headers = new HttpHeaders();
		String token = "Xjn5GEsYsQySAsr7APqj";
		headers.set("X-Auth-Token", token);
		
		HttpRequest request = requestFactory.buildGetRequest(new GenericUrl("http://localhost:8000/domains.json"));
		request.setHeaders(headers);
		Type t = new TypeToken<List<Domain>>() {}.getType();
		List<Domain> lista = (List<Domain>) request.execute().parseAs(t);
		for (Domain d: lista) {
			System.out.println(d);
			System.out.println(d.getName());
		}
	}
	
	private static class DnsApiCustomParser implements ObjectParser {
		
		private final GsonBuilder builder;
		
		public DnsApiCustomParser() {
			builder = new GsonBuilder()
				.setPrettyPrinting()
				.registerTypeAdapter(Domain.class, new TypeAdapter<Domain>() {

					@Override
					public void write(com.google.gson.stream.JsonWriter out,
							Domain value) throws IOException {
						// TODO Auto-generated method stub
						
					}

					@Override
					public Domain read(JsonReader in)
							throws IOException {
						System.out.println(in.peek());
						if ("domain".equals(in.nextName())) {
							Domain domain = new Domain();
							return domain;
						}
						return null;
					}
				});
			
			List<Domain> lista = new ArrayList<Domain>();
			Domain d = new Domain();
			d.setName("nome do dominio");
			lista.add(d);
			lista.add(d);
			System.out.println(builder.create().toJson(lista));
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public <T> T parseAndClose(InputStream in, Charset charset,
				Class<T> dataClass) throws IOException {
			Reader reader = new InputStreamReader(in, charset);
			return this.parseAndClose(reader, dataClass);
		}

		@Override
		public Object parseAndClose(InputStream in, Charset charset,
				Type dataType) throws IOException {
			Reader reader = new InputStreamReader(in, charset);
			return this.parseAndClose(reader, dataType);
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T parseAndClose(Reader reader, Class<T> dataClass)
				throws IOException {
			return (T) parseAndClose(reader, dataClass.getGenericSuperclass());
		}
		
		private String readerToString(Reader reader) throws IOException {
			StringBuilder str = new StringBuilder();
			char[] cbuf = new char[1024];
			while (true) {
				int chars = reader.read(cbuf);
				if (chars == -1) {
					break;
				}
				str.append(cbuf, 0, chars);
			}
			return str.toString();
		}

		@Override
		public Object parseAndClose(Reader reader, Type dataType)
				throws IOException {
			return builder.create().fromJson(reader, dataType);
//			System.out.println(dataType.getClass());
//			if (dataType instanceof ParameterizedType) {
//				ParameterizedType ptype = (ParameterizedType) dataType;
//				if (Collection.class.isAssignableFrom((Class<?>) ptype.getRawType())) {
//					// xstream não suporta json array sem elemento raiz, dai adiciono à mão um elemento raiz
//					String jsonRoot = this.readerToString(reader);
//					String json = "{ lista: " + jsonRoot + " }";
//				}
//			}
//			return builder.fromXML(reader);
		}
		
	}
	

}
