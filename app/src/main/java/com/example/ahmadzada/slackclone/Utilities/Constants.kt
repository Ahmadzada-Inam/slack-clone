package com.example.ahmadzada.slackclone.Utilities

const val BASE_URL = "https://slackyclone.herokuapp.com/v1/"

const val SOCKET_URL = "https://slackyclone.herokuapp.com/"
const val REGISTER_URL = "${BASE_URL}account/register/"
const val LOGIN_URL = "${BASE_URL}account/login"
const val CREATE_URL = "${BASE_URL}user/add"
const val FIND_USER_URL = "${BASE_URL}user/byEmail/"
const val GET_CHANNELS_URL = "${BASE_URL}channel/"
const val GET_CHANNEL_MESSAGE_URL = "${BASE_URL}message/byChannel/"

const val BROADCAST_USER_CHANGE_INTENT = "BROADCAST_USER_CHANGE_INTENT"