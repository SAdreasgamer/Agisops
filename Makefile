.PHONY: dev down java-build java-test python-test lint migrate

dev:
	docker compose up -d

down:
	docker compose down

java-build:
	cd java-services && mvn clean package -DskipTests

java-test:
	cd java-services && mvn verify

python-test:
	cd python-services && ./venv/bin/pytest tests/ -v

lint:
	cd java-services && mvn spotbugs:check
	cd python-services && ./venv/bin/ruff check .

migrate:
	cd java-services && mvn flyway:migrate -pl common
