CREATE TABLE "Team" (
	"Name"	TEXT,
	"team_code"	TEXT NOT NULL,
	PRIMARY KEY("team_code"),
	FOREIGN KEY("team_code") REFERENCES "Player"("team_code")
);

CREATE TABLE "Player" (
	"Ranking"	INT,
	"Age"	INT,
	"Fname"	TEXT,
	"Lname"	TEXT,
	"team_code"	TEXT,
	"Position"	TEXT,
	"GamesP"	INT,
	"Goals"	INT,
	"Assists"	INT,
	"Tpoints"	INT,
	PRIMARY KEY("Ranking")
);

CREATE TABLE "Coaches" (
	"coach_id"	INTEGER,
	"team_code"	TEXT,
	"fname"	TEXT,
	"lname"	TEXT,
	PRIMARY KEY("coach_id"),
	FOREIGN KEY("team_code") REFERENCES "Team"("team_code")
);
