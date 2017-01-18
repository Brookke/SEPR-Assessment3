PRAGMA foreign_keys=ON;
BEGIN TRANSACTION;
CREATE TABLE means (
	id INTEGER NOT NULL, 
	description TEXT NOT NULL, 
	PRIMARY KEY (id)
);
CREATE TABLE room_types (
	id INTEGER NOT NULL, 
	name TEXT NOT NULL, 
	minimum_count INTEGER DEFAULT '0' NOT NULL, 
	maximum_count INTEGER, 
	PRIMARY KEY (id)
);
CREATE TABLE resources (
	id INTEGER NOT NULL, 
	filename TEXT NOT NULL, 
	PRIMARY KEY (id)
);
CREATE TABLE clues (
	id INTEGER NOT NULL, 
	description TEXT NOT NULL, 
	implies_means_rating INTEGER DEFAULT '0' NOT NULL, 
	implies_motive_rating INTEGER DEFAULT '0' NOT NULL, 
	PRIMARY KEY (id)
);
CREATE TABLE questioning_styles (
	id INTEGER NOT NULL, 
	description TEXT NOT NULL, 
	PRIMARY KEY (id)
);
CREATE TABLE motives (
	id INTEGER NOT NULL, 
	description TEXT NOT NULL, 
	PRIMARY KEY (id)
);
CREATE TABLE question_intentions (
	id INTEGER NOT NULL, 
	description TEXT NOT NULL, 
	starting_question BOOLEAN NOT NULL, 
	PRIMARY KEY (id), 
	CHECK (starting_question IN (0, 1))
);
CREATE TABLE costumes (
	id INTEGER NOT NULL, 
	description TEXT NOT NULL, 
	resource INTEGER, 
	PRIMARY KEY (id), 
	FOREIGN KEY(resource) REFERENCES resources (id) ON DELETE SET NULL
);
CREATE TABLE clue_motive_requirement (
	id INTEGER NOT NULL, 
	motive INTEGER NOT NULL, 
	clue INTEGER NOT NULL, 
	PRIMARY KEY (id), 
	FOREIGN KEY(motive) REFERENCES motives (id) ON DELETE CASCADE, 
	FOREIGN KEY(clue) REFERENCES clues (id) ON DELETE CASCADE
);
CREATE TABLE props (
	id INTEGER NOT NULL, 
	name TEXT NOT NULL, 
	description TEXT NOT NULL, 
	must_be_clue BOOLEAN DEFAULT '0' NOT NULL, 
	resource INTEGER, 
	PRIMARY KEY (id), 
	CHECK (must_be_clue IN (0, 1)), 
	FOREIGN KEY(resource) REFERENCES resources (id) ON DELETE SET NULL
);
CREATE TABLE question_and_responses (
	id INTEGER NOT NULL, 
	question_text TEXT NOT NULL, 
	must_be_clue BOOLEAN DEFAULT '1' NOT NULL, 
	question_intention INTEGER NOT NULL, 
	question_style INTEGER NOT NULL, 
	PRIMARY KEY (id), 
	CHECK (must_be_clue IN (0, 1)), 
	FOREIGN KEY(question_intention) REFERENCES question_intentions (id) ON DELETE CASCADE, 
	FOREIGN KEY(question_style) REFERENCES questioning_styles (id) ON DELETE CASCADE
);
CREATE TABLE room_templates (
	id INTEGER NOT NULL, 
	width INTEGER DEFAULT '1' NOT NULL, 
	height INTEGER DEFAULT '1' NOT NULL, 
	selection_weight INTEGER DEFAULT '100' NOT NULL, 
	background_resource INTEGER, 
	room_type INTEGER NOT NULL, 
	PRIMARY KEY (id), 
	FOREIGN KEY(background_resource) REFERENCES room_templates (id) ON DELETE SET NULL, 
	FOREIGN KEY(room_type) REFERENCES room_types (id) ON DELETE CASCADE
);
CREATE TABLE characters (
	id INTEGER NOT NULL, 
	name TEXT NOT NULL, 
	description TEXT NOT NULL, 
	selection_weight INTEGER DEFAULT '100' NOT NULL, 
	resource INTEGER, 
	PRIMARY KEY (id), 
	FOREIGN KEY(resource) REFERENCES resources (id) ON DELETE SET NULL
);
CREATE TABLE clue_means_requirements (
	id INTEGER NOT NULL, 
	means INTEGER NOT NULL, 
	clue INTEGER NOT NULL, 
	PRIMARY KEY (id), 
	FOREIGN KEY(means) REFERENCES means (id) ON DELETE CASCADE, 
	FOREIGN KEY(clue) REFERENCES clues (id) ON DELETE CASCADE
);
CREATE TABLE character_costume_links (
	id INTEGER NOT NULL, 
	character INTEGER NOT NULL, 
	costume INTEGER NOT NULL, 
	PRIMARY KEY (id), 
	FOREIGN KEY(character) REFERENCES characters (id) ON DELETE CASCADE, 
	FOREIGN KEY(costume) REFERENCES costumes (id) ON DELETE CASCADE
);
CREATE TABLE clue_victim_requirements (
	id INTEGER NOT NULL, 
	victim INTEGER NOT NULL, 
	clue INTEGER NOT NULL, 
	PRIMARY KEY (id), 
	FOREIGN KEY(victim) REFERENCES characters (id) ON DELETE CASCADE, 
	FOREIGN KEY(clue) REFERENCES clues (id) ON DELETE CASCADE
);
CREATE TABLE character_motive_links (
	id INTEGER NOT NULL, 
	murderer INTEGER NOT NULL, 
	victim INTEGER NOT NULL, 
	motive INTEGER NOT NULL, 
	selection_weight INTEGER DEFAULT '100' NOT NULL, 
	PRIMARY KEY (id), 
	FOREIGN KEY(murderer) REFERENCES characters (id) ON DELETE CASCADE, 
	FOREIGN KEY(victim) REFERENCES characters (id) ON DELETE CASCADE, 
	FOREIGN KEY(motive) REFERENCES motives (id) ON DELETE CASCADE
);
CREATE TABLE dialogue_text_screens (
	id INTEGER NOT NULL, 
	text TEXT NOT NULL, 
	dialogue_position INTEGER NOT NULL, 
	response_parent INTEGER NOT NULL, 
	PRIMARY KEY (id), 
	FOREIGN KEY(response_parent) REFERENCES question_and_responses (id) ON DELETE CASCADE
);
CREATE TABLE follow_up_questions (
	id INTEGER NOT NULL, 
	following_question INTEGER NOT NULL, 
	preceding_response INTEGER NOT NULL, 
	PRIMARY KEY (id), 
	FOREIGN KEY(following_question) REFERENCES question_and_responses (id) ON DELETE CASCADE, 
	FOREIGN KEY(preceding_response) REFERENCES follow_up_questions (id) ON DELETE CASCADE
);
CREATE TABLE protoprops (
	id INTEGER NOT NULL, 
	x_pos FLOAT NOT NULL, 
	y_pos FLOAT NOT NULL, 
	room_template_id INTEGER NOT NULL, 
	PRIMARY KEY (id), 
	FOREIGN KEY(room_template_id) REFERENCES room_templates (id) ON DELETE CASCADE
);
CREATE TABLE clue_murderer_requirements (
	id INTEGER NOT NULL, 
	murderer INTEGER NOT NULL, 
	clue INTEGER NOT NULL, 
	PRIMARY KEY (id), 
	FOREIGN KEY(murderer) REFERENCES characters (id) ON DELETE CASCADE, 
	FOREIGN KEY(clue) REFERENCES clues (id) ON DELETE CASCADE
);
CREATE TABLE prop_clue_implication (
	id INTEGER NOT NULL, 
	prop INTEGER NOT NULL, 
	clue INTEGER NOT NULL, 
	PRIMARY KEY (id), 
	FOREIGN KEY(prop) REFERENCES props (id) ON DELETE CASCADE, 
	FOREIGN KEY(clue) REFERENCES clues (id) ON DELETE CASCADE
);
CREATE TABLE response_clue_implication (
	id INTEGER NOT NULL, 
	response INTEGER NOT NULL, 
	clue INTEGER NOT NULL, 
	PRIMARY KEY (id), 
	FOREIGN KEY(response) REFERENCES question_and_responses (id) ON DELETE CASCADE, 
	FOREIGN KEY(clue) REFERENCES clues (id) ON DELETE CASCADE
);
CREATE TABLE character_means_links (
	id INTEGER NOT NULL, 
	character INTEGER NOT NULL, 
	means INTEGER NOT NULL, 
	selection_weight INTEGER DEFAULT '100' NOT NULL, 
	PRIMARY KEY (id), 
	FOREIGN KEY(character) REFERENCES characters (id) ON DELETE CASCADE, 
	FOREIGN KEY(means) REFERENCES means (id) ON DELETE CASCADE
);
CREATE TABLE potential_prop_instances (
	id INTEGER NOT NULL, 
	prop INTEGER NOT NULL, 
	protoprop INTEGER NOT NULL, 
	PRIMARY KEY (id), 
	FOREIGN KEY(prop) REFERENCES props (id) ON DELETE CASCADE, 
	FOREIGN KEY(protoprop) REFERENCES protoprops (id) ON DELETE CASCADE
);
COMMIT;
