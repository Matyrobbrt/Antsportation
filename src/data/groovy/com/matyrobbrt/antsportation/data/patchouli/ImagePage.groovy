package com.matyrobbrt.antsportation.data.patchouli

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.matyrobbrt.lib.datagen.patchouli.page.IPatchouliPage

class ImagePage implements IPatchouliPage {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create()

	private final List<String> images = new ArrayList<>()
	public String title
	public String text
	public boolean border

	void image(String... images) {
		this.images.addAll(images)
	}

	@Override
	String getType() {
		return "patchouli:image";
	}

	@Override
	JsonElement serialize() {
		JsonObject object = new JsonObject()
		object.addProperty('type', getType())
		
		object.add('images', GSON.toJsonTree(images))
		if (title)
			object.addProperty('title', title)
		if (text)
			object.addProperty('text', text)
		object.addProperty('border', border)

		return object
	}

	static ImagePage image(@DelegatesTo(value = ImagePage, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		final page = new ImagePage()
		closure.resolveStrategy = Closure.DELEGATE_FIRST
		closure.delegate = page
		closure(page)
		return page
	}
}
