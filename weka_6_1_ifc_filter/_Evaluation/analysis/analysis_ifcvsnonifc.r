mydata=read.csv("/Users/micha/Dropbox/Science/Projects/_Ongoing/2014-10-04 ESWA/Revision201505/weka_6_1_nlr/_Evaluation2/analysis/data.csv")

#mydata=read.csv("C:/Users/kaum/Documents/NetBeansProjects/IFC_Source_NetBeansProject/weka_6_1/_Evaluation/analysis/test")

library(MASS) 

ifc <- mydata[mydata$ifc=="ifc" & mydata$classifier=="linreg",]
nonifc <- mydata[mydata$ifc=="nonifc" & mydata$classifier=="linreg",]
comparison <- merge(ifc, nonifc, c("dataset","classifier"))
wilcox.test(comparison$rmse.x, comparison$rmse.y, alternative="less", paired=TRUE)
mean(comparison$rmse.x)
mean(comparison$rmse.y)

ifc <- mydata[mydata$ifc=="ifc" & mydata$classifier=="logreg",]
nonifc <- mydata[mydata$ifc=="nonifc" & mydata$classifier=="logreg",]
comparison <- merge(ifc, nonifc, c("dataset","classifier"))
wilcox.test(comparison$rmse.x, comparison$rmse.y, alternative="less", paired=TRUE)
mean(comparison$rmse.x)
mean(comparison$rmse.y)

library(plyr) 

cdata <- ddply(ifc, c("dataset"), summarise,
               median = median(dataset, na.rm=TRUE),
               sd   = sd(dataset, na.rm=TRUE)
               

ifc <- mydata[mydata[3]=="ifc",]
nonifc <- mydata[mydata[3]=="nonifc",]
comparison <- merge(ifc, nonifc, c("dataset","classifier"))

#wilcox.test(mydata$nonifc.time1, mydata$ifc.time1, alternative="less", paired=FALSE)

#wilcox.test(mydata$nonifc.time2, mydata$ifc.time2, alternative="less", paired=FALSE)

wilcox.test(mydata$nonifc.rmse, mydata$ifc.rmse, alternative="two.sided", paired=FALSE)

mean(mydata$nonifc.rmse)

mean(mydata$ifc.rmse)

wilcox.test(mydata$nonifc.time1+mydata$nonifc.time2, mydata$ifc.time1+mydata$ifc.time2, alternative="less", paired=FALSE)

mean(mydata$nonifc.time1+mydata$nonifc.time2)

mean(mydata$ifc.time1+mydata$ifc.time2)



#######

#mydata2 <- subset(mydata, mydata$classifier=="logreg")

#library(Hmisc)

#rcorr(mydata$delta.rmse, mydata$nlin, type="pearson")

#plot(mydata$nlin, mydata$delta.rmse, ylab="RMSE difference by IFC-Filter", xlab="RMSE of linear regression model ", pch=19)

#abline(lm(mydata$delta.rmse ~ mydata$nlin))

#rcorr((mydata$ifc.rmse-mydata$nonifc.rmse), mydata$nlin, type="pearson")

#plot(mydata$nlin, mydata$ifc.rmse-mydata$nonifc.rmse, ylab="RMSE difference by IFC-Filter", xlab="RMSE of linear regression model ", pch=19)

#abline(lm((mydata$ifc.rmse-mydata$nonifc.rmse) ~ mydata$nlin))

#mydata$time.ifc <- (mydata$ifc.time1+mydata$ifc.time2)
#mydata$time.nonifc <- (mydata$nonifc.time1+mydata$nonifc.time2)
#mydata$delta.time <- (mydata$time.ifc-mydata$time.nonifc)/mydata$time.nonifc

#rcorr(mydata$delta.time, mydata$time.nonifc , type="pearson")
#rcorr(mydata$delta.time, mydata$nlin , type="pearson")

#plot(mydata$time.nonifc, mydata$delta.time, ylab="Time reduction by IFC-Filter", xlab="Computation time on original data", pch=19)




