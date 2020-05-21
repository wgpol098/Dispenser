using System;
using Microsoft.EntityFrameworkCore.Migrations;

namespace WebApplication1.Migrations
{
    public partial class init : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "Accounts",
                columns: table => new
                {
                    Id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Login = table.Column<string>(nullable: true),
                    Password = table.Column<string>(nullable: true),
                    Name = table.Column<string>(nullable: true),
                    TypeAccount = table.Column<int>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Accounts", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "Dispensers",
                columns: table => new
                {
                    Id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    IdDispenser = table.Column<int>(nullable: false),
                    NoWindow = table.Column<string>(nullable: true),
                    NoUpdate = table.Column<int>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Dispensers", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "History",
                columns: table => new
                {
                    Id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    IdDispenser = table.Column<int>(nullable: false),
                    DateAndTime = table.Column<DateTime>(nullable: false),
                    NoWindow = table.Column<int>(nullable: false),
                    Description = table.Column<string>(nullable: true),
                    Flag = table.Column<int>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_History", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "ListOfDispensers",
                columns: table => new
                {
                    Id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    IdAccount = table.Column<int>(nullable: false),
                    IdDispenser = table.Column<int>(nullable: false),
                    Name = table.Column<string>(nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_ListOfDispensers", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "Plans",
                columns: table => new
                {
                    Id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    IdDispenser = table.Column<int>(nullable: false),
                    DateAndTime = table.Column<DateTime>(nullable: false),
                    NoWindow = table.Column<int>(nullable: false),
                    Description = table.Column<string>(nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Plans", x => x.Id);
                });

            migrationBuilder.InsertData(
                table: "Accounts",
                columns: new[] { "Id", "Login", "Name", "Password", "TypeAccount" },
                values: new object[,]
                {
                    { 1, "TestGuy123", "Andrzej Krawczyk", "Tak123", 1 },
                    { 2, "TG123456", "Mateusz Wałek", "Nie321", 1 },
                    { 3, "TTeesstt", "Tomek Kowalski", "Haslo", 1 },
                    { 4, "Tetestst", "Paweł Nowak", "Test", 1 },
                    { 5, "GuyGuyGuy", "Jakub Lewandowski", "Somsiad", 1 },
                    { 6, "TempTemp", "Marek Pracowity", "Viagra", 3 },
                    { 7, "TestAccount", "Franek Golas", "Wojna", 2 },
                    { 8, "qwerty", "Adam Mickiewicz", "Pisarz", 2 },
                    { 9, "qwerty123", "Jan Kazimierz Kretacz", "Test109", 1 }
                });

            migrationBuilder.InsertData(
                table: "Dispensers",
                columns: new[] { "Id", "IdDispenser", "NoUpdate", "NoWindow" },
                values: new object[,]
                {
                    { 9, 109, 45, "1000010" },
                    { 8, 108, 40, "0001001" },
                    { 7, 107, 35, "1000001" },
                    { 6, 106, 30, "0001000" },
                    { 5, 105, 25, "0000001" },
                    { 4, 104, 20, "1000000" },
                    { 3, 103, 15, "0100010" },
                    { 2, 102, 10, "1111100" },
                    { 1, 101, 5, "1110000" }
                });

            migrationBuilder.InsertData(
                table: "History",
                columns: new[] { "Id", "DateAndTime", "Description", "Flag", "IdDispenser", "NoWindow" },
                values: new object[,]
                {
                    { 19, new DateTime(2020, 3, 7, 21, 0, 1, 0, DateTimeKind.Unspecified), "Iladian", 1, 109, 2 },
                    { 17, new DateTime(2020, 3, 6, 21, 0, 1, 0, DateTimeKind.Unspecified), "Stimea", 1, 109, 7 },
                    { 16, new DateTime(2020, 3, 6, 8, 0, 1, 0, DateTimeKind.Unspecified), "Limetic", 1, 108, 6 },
                    { 15, new DateTime(2020, 3, 5, 17, 0, 1, 0, DateTimeKind.Unspecified), "Maxigra", 1, 107, 5 },
                    { 14, new DateTime(2020, 3, 5, 8, 0, 1, 0, DateTimeKind.Unspecified), "Iladian", -1, 106, 4 },
                    { 13, new DateTime(2020, 3, 5, 7, 0, 1, 0, DateTimeKind.Unspecified), "Stimea", 1, 105, 3 },
                    { 12, new DateTime(2020, 3, 4, 18, 0, 1, 0, DateTimeKind.Unspecified), "Limetic", 0, 104, 3 },
                    { 11, new DateTime(2020, 3, 4, 14, 0, 1, 0, DateTimeKind.Unspecified), "Maxigra", 1, 103, 2 },
                    { 10, new DateTime(2020, 3, 4, 9, 0, 1, 0, DateTimeKind.Unspecified), "Iladian", -1, 102, 2 },
                    { 18, new DateTime(2020, 3, 7, 10, 0, 1, 0, DateTimeKind.Unspecified), "Maxigra", -1, 109, 1 },
                    { 8, new DateTime(2020, 3, 3, 13, 0, 1, 0, DateTimeKind.Unspecified), "Limetic", 1, 102, 1 },
                    { 7, new DateTime(2020, 3, 3, 8, 0, 1, 0, DateTimeKind.Unspecified), "Maxigra", 1, 103, 7 },
                    { 6, new DateTime(2020, 3, 2, 18, 0, 1, 0, DateTimeKind.Unspecified), "Iladian", -1, 104, 6 },
                    { 5, new DateTime(2020, 3, 2, 9, 0, 1, 0, DateTimeKind.Unspecified), "Stimea", 1, 105, 5 },
                    { 4, new DateTime(2020, 3, 2, 6, 0, 1, 0, DateTimeKind.Unspecified), "Limetic", 1, 106, 4 },
                    { 3, new DateTime(2020, 3, 1, 16, 0, 1, 0, DateTimeKind.Unspecified), "Maxigra", 0, 107, 3 },
                    { 2, new DateTime(2020, 3, 1, 11, 0, 1, 0, DateTimeKind.Unspecified), "Iladian", 1, 108, 2 },
                    { 1, new DateTime(2020, 3, 1, 9, 0, 1, 0, DateTimeKind.Unspecified), "Stimea", 0, 109, 1 },
                    { 9, new DateTime(2020, 3, 3, 17, 0, 1, 0, DateTimeKind.Unspecified), "Stimea", 1, 101, 1 }
                });

            migrationBuilder.InsertData(
                table: "ListOfDispensers",
                columns: new[] { "Id", "IdAccount", "IdDispenser", "Name" },
                values: new object[,]
                {
                    { 16, 8, 109, "p" },
                    { 15, 8, 102, "o" },
                    { 14, 7, 109, "n" },
                    { 12, 6, 104, "l" },
                    { 11, 6, 102, "k" },
                    { 10, 6, 101, "j" },
                    { 9, 9, 109, "i" },
                    { 13, 7, 103, "m" },
                    { 7, 7, 107, "g" },
                    { 6, 6, 106, "f" },
                    { 5, 5, 105, "e" },
                    { 4, 4, 104, "d" },
                    { 3, 3, 103, "c" },
                    { 2, 2, 102, "b" },
                    { 1, 1, 101, "a" },
                    { 8, 8, 108, "h" }
                });

            migrationBuilder.InsertData(
                table: "Plans",
                columns: new[] { "Id", "DateAndTime", "Description", "IdDispenser", "NoWindow" },
                values: new object[,]
                {
                    { 17, new DateTime(2020, 3, 20, 17, 30, 1, 0, DateTimeKind.Unspecified), "Eutanazol", 109, 6 },
                    { 16, new DateTime(2020, 3, 20, 16, 0, 1, 0, DateTimeKind.Unspecified), "Eutanazol", 109, 1 },
                    { 15, new DateTime(2020, 3, 22, 16, 0, 1, 0, DateTimeKind.Unspecified), "lol", 108, 7 },
                    { 14, new DateTime(2020, 3, 20, 5, 0, 1, 0, DateTimeKind.Unspecified), "lol", 108, 4 },
                    { 13, new DateTime(2020, 3, 20, 2, 0, 1, 0, DateTimeKind.Unspecified), "brain", 107, 7 },
                    { 12, new DateTime(2020, 3, 20, 1, 0, 1, 0, DateTimeKind.Unspecified), "brain", 107, 1 },
                    { 11, new DateTime(2020, 3, 25, 9, 0, 1, 0, DateTimeKind.Unspecified), "ddd", 106, 4 },
                    { 10, new DateTime(2020, 3, 20, 4, 0, 1, 0, DateTimeKind.Unspecified), "ccc", 105, 7 },
                    { 6, new DateTime(2020, 3, 21, 6, 0, 1, 0, DateTimeKind.Unspecified), "IBUM", 102, 5 },
                    { 8, new DateTime(2020, 3, 20, 23, 0, 1, 0, DateTimeKind.Unspecified), "aaa", 103, 6 },
                    { 7, new DateTime(2020, 3, 20, 4, 0, 1, 0, DateTimeKind.Unspecified), "aaa", 103, 2 },
                    { 5, new DateTime(2020, 3, 20, 18, 0, 1, 0, DateTimeKind.Unspecified), "IBUM", 102, 3 },
                    { 4, new DateTime(2020, 3, 20, 6, 0, 1, 0, DateTimeKind.Unspecified), "IBUM", 102, 1 },
                    { 3, new DateTime(2020, 3, 22, 13, 0, 1, 0, DateTimeKind.Unspecified), "Apap", 101, 3 },
                    { 2, new DateTime(2020, 3, 21, 13, 0, 1, 0, DateTimeKind.Unspecified), "Apap", 101, 2 },
                    { 1, new DateTime(2020, 3, 20, 13, 0, 1, 0, DateTimeKind.Unspecified), "Apap", 101, 1 },
                    { 18, new DateTime(2020, 3, 20, 15, 30, 1, 0, DateTimeKind.Unspecified), "Apap", 102, 2 },
                    { 9, new DateTime(2020, 3, 20, 5, 0, 1, 0, DateTimeKind.Unspecified), "bbb", 104, 1 },
                    { 19, new DateTime(2020, 3, 20, 7, 30, 1, 0, DateTimeKind.Unspecified), "Apap", 102, 4 }
                });
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "Accounts");

            migrationBuilder.DropTable(
                name: "Dispensers");

            migrationBuilder.DropTable(
                name: "History");

            migrationBuilder.DropTable(
                name: "ListOfDispensers");

            migrationBuilder.DropTable(
                name: "Plans");
        }
    }
}
