package net.mcft.copy.vanilladj.config.setting.list;

public interface IElementHandler<T> {
	
	T read(String str) throws Exception;
	
	String write(T value);
	
}
