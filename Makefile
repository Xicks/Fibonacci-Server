POSTGRES_PORT_BIND=-p 5432:5432
REDIS_PORT_BIND=-p 6379:6379

help: ## Show this help
	@fgrep -h "##" $(MAKEFILE_LIST) | fgrep -v fgrep | sed -E 's/:.*##/:\t/g'

stack: ## Start the stack
	make db
	make cache

db: ## Start PostgreSQL
	@docker run -d --rm --name location-postgres ${POSTGRES_PORT_BIND} postgres

cache: ## Start Redis
	@docker run -d --rm --name location-redis ${REDIS_PORT_BIND} redis:5.0.6

stop: ## Stop the stack
	@-docker stop location-postgres
	@-docker stop location-redis
	@echo "Containers stopped!"
