FROM node:latest as node
WORKDIR /app

COPY package*.json ./

RUN npm install
COPY . .

RUN npm run build

FROM nginx:alpine

EXPOSE 80 4200

COPY --from=node /app/dist/front /usr/share/nginx/html

COPY ./nginx/default.conf /etc/nginx/conf.d/default.conf

