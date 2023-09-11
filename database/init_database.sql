CREATE TABLE public.users (
    id VARCHAR(36) NOT NULL,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(60) NOT NULL,
    role VARCHAR(15) NULL,
    healthdata BOOLEAN,
    PRIMARY KEY (id)
);

CREATE TABLE public.health (
    id VARCHAR(36) NOT NULL,
    creator VARCHAR(36) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    height FLOAT, -- User's height in meters
    weight FLOAT, -- User's weight in kilograms
    bmi FLOAT,    -- Body Mass Index
    target_weight FLOAT,
    target_calories INTEGER, -- Target daily calories
    target_steps INTEGER, -- Target daily steps
    PRIMARY KEY (id)
);

CREATE TABLE public.dailytrainingandcalories (
    id VARCHAR(36) NOT NULL,
    creator VARCHAR(36) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    current_weight FLOAT,
    daily_calories INTEGER, -- Eaten daily calories
    daily_steps INTEGER, -- Done daily steps
    PRIMARY KEY (id)
)