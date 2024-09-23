FROM node:18-alpine as builder
WORKDIR '/app'
COPY package.json .
RUN yarn install
COPY . /app
RUN yarn build

FROM nginx
COPY --from=builder /app/build /usr/share/nginx/html
COPY default.conf /etc/nginx/conf.d/default.conf
